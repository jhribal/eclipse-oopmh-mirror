/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 425815 - Add support for secure context variables
 *    Ericsson AB (Julian Enoch) - Bug 434525 - Allow prompted variables to be pre-populated
 */
package org.eclipse.oomph.internal.setup.core;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.setup.core.bundle.SetupCorePlugin;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.ResourceCopyTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.WorkspaceTask;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.log.ProgressLogFilter;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.Resource.Internal;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.VersionRange;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class SetupTaskPerformer extends AbstractSetupTaskContext
{
  public static final boolean REMOTE_DEBUG = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_REMOTE_DEBUG);

  public static final Adapter RULE_VARIABLE_ADAPTER = new AdapterImpl();

  private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private static final String VARIABLE_ANNOTATION_SOURCE = "http://www.eclipse.org/oomph/setup/Variable";

  private static final String RULE_VARIABLE_ANNOTATION_SOURCE = "http://www.eclipse.org/oomph/setup/RuleVariable";

  private static final Pattern INSTALLABLE_UNIT_WITH_RANGE_PATTERN = Pattern.compile("([^\\[\\(]*)(.*)");

  private static boolean NEEDS_PATH_SEPARATOR_CONVERSION = File.separatorChar == '\\';

  private static final Pattern STRING_EXPANSION_PATTERN = Pattern.compile("\\$(\\{([^${}|]+)(\\|([^}]+))?}|\\$)");

  private static ProgressLog progress;

  private boolean canceled;

  private EList<SetupTask> triggeredSetupTasks;

  private Map<EObject, EObject> copyMap;

  private EList<SetupTask> neededSetupTasks;

  private List<String> logMessageBuffer;

  private PrintStream logStream;

  private ProgressLogFilter logFilter = new ProgressLogFilter();

  private List<EStructuralFeature.Setting> unresolvedSettings = new ArrayList<EStructuralFeature.Setting>();

  private List<VariableTask> unresolvedVariables = new ArrayList<VariableTask>();

  private List<VariableTask> resolvedVariables = new ArrayList<VariableTask>();

  private List<VariableTask> appliedRuleVariables = new ArrayList<VariableTask>();

  private Map<String, VariableTask> allVariables = new HashMap<String, VariableTask>();

  private Set<String> undeclaredVariables = new HashSet<String>();

  private Map<VariableTask, EAttribute> ruleAttributes = new HashMap<VariableTask, EAttribute>();

  private Map<VariableTask, EAttribute> ruleBasedAttributes = new HashMap<VariableTask, EAttribute>();

  private List<AttributeRule> attributeRules = new ArrayList<AttributeRule>();

  private ComposedAdapterFactory adapterFactory = EMFUtil.createAdapterFactory();

  public SetupTaskPerformer(URIConverter uriConverter, SetupPrompter prompter, Trigger trigger, SetupContext setupContext, Stream stream)
  {
    super(uriConverter, prompter, trigger, setupContext);
    initTriggeredSetupTasks(stream, true);
  }

  public SetupTaskPerformer(URIConverter uriConverter, SetupPrompter prompter, Trigger trigger, SetupContext setupContext, EList<SetupTask> triggeredSetupTasks)
  {
    super(uriConverter, prompter, trigger, setupContext);
    this.triggeredSetupTasks = triggeredSetupTasks;
    initTriggeredSetupTasks(null, false);
  }

  private <K, V> void add(Map<K, Set<V>> map, K key, V value)
  {
    Set<V> set = map.get(key);
    if (set == null)
    {
      set = new HashSet<V>();
      map.put(key, set);
    }
    set.add(value);
  }

  private <K, V> void addAll(Map<K, Set<V>> map, K key, Collection<? extends V> values)
  {
    Set<V> set = map.get(key);
    if (set == null)
    {
      set = new HashSet<V>();
      map.put(key, set);
    }
    set.addAll(values);
  }

  private void initTriggeredSetupTasks(Stream stream, boolean firstPhase)
  {
    Trigger trigger = getTrigger();

    User user = getUser();

    // Gather all possible tasks.
    // Later this will be filtered to only the triggered tasks.
    // This approach ensures that implicit variables for all tasks (even for untriggered tasks) are created with the right values.
    if (firstPhase)
    {
      triggeredSetupTasks = new BasicEList<SetupTask>(getSetupTasks(stream, null));
    }

    EList<SetupTask> setupTasks = triggeredSetupTasks; // Debug help: ensure that the field is visible on the local stack frame

    if (firstPhase)
    {
      // 1. Collect and flatten all tasks
      Set<EClass> eClasses = new LinkedHashSet<EClass>();
      Map<EClass, Set<SetupTask>> instances = new HashMap<EClass, Set<SetupTask>>();
      Set<String> keys = new HashSet<String>();
      for (SetupTask setupTask : setupTasks)
      {
        EClass eClass = setupTask.eClass();
        add(instances, eClass, setupTask);
        eClasses.add(eClass);
        for (EClass eSuperType : eClass.getEAllSuperTypes())
        {
          if (SetupPackage.Literals.SETUP_TASK.isSuperTypeOf(eSuperType))
          {
            eClasses.add(eSuperType);
            add(instances, eSuperType, setupTask);
          }
        }

        if (setupTask instanceof InstallationTask)
        {
          Resource resource = getInstallation().eResource();
          if (resource != null)
          {
            URI uri = resource.getURI();
            if (!SetupContext.INSTALLATION_SETUP_FILE_NAME_URI.equals(uri))
            {
              InstallationTask installationTask = (InstallationTask)setupTask;
              installationTask.setLocation(uri.trimSegments(4).toFileString());
            }
          }
        }
        else if (setupTask instanceof WorkspaceTask)
        {
          Resource resource = getWorkspace().eResource();
          if (resource != null)
          {
            URI uri = resource.getURI();
            if (!SetupContext.WORKSPACE_SETUP_FILE_NAME_URI.equals(uri))
            {
              WorkspaceTask workspaceTask = (WorkspaceTask)setupTask;
              workspaceTask.setLocation(uri.trimSegments(4).toFileString());
            }
          }
        }
        else if (setupTask instanceof VariableTask)
        {
          VariableTask variable = (VariableTask)setupTask;
          keys.add(variable.getName());
        }
      }

      for (EClass eClass : eClasses)
      {
        // 1.1. Collect enablement info to synthesize P2Tasks that are placed at the head of the task list
        for (EAnnotation eAnnotation : eClass.getEAnnotations())
        {
          String source = eAnnotation.getSource();
          if (AnnotationConstants.ANNOTATION_ENABLEMENT.equals(source))
          {
            String variableName = eAnnotation.getDetails().get(AnnotationConstants.KEY_VARIABLE_NAME);
            String p2RepositoryLocation = eAnnotation.getDetails().get(AnnotationConstants.KEY_REPOSITORY);

            VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
            variable.setName(variableName);
            variable.setValue(p2RepositoryLocation);
            setupTasks.add(0, variable);

            P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
            EList<Requirement> requirements = p2Task.getRequirements();
            for (String requirementSpecification : eAnnotation.getDetails().get(AnnotationConstants.KEY_INSTALLABLE_UNITS).split("\\s"))
            {
              Matcher matcher = INSTALLABLE_UNIT_WITH_RANGE_PATTERN.matcher(requirementSpecification);
              if (matcher.matches())
              {
                Requirement requirement = P2Factory.eINSTANCE.createRequirement(matcher.group(1));
                String versionRange = matcher.group(2);
                if (!StringUtil.isEmpty(versionRange))
                {
                  requirement.setVersionRange(new VersionRange(versionRange));
                }

                requirements.add(requirement);
              }
            }

            Repository repository = P2Factory.eINSTANCE.createRepository("${" + variableName + "}");
            p2Task.getRepositories().add(repository);

            // Ensure that these are first so that these are the targets for merging rather than the sources.
            // The latter causes problems in the copier.
            setupTasks.add(0, p2Task);
          }
        }

        if (user.eResource() != null)
        {
          // 1.2. Determine whether new rules need to be created
          for (EAttribute eAttribute : eClass.getEAttributes())
          {
            if (eAttribute.getEType().getInstanceClass() == String.class)
            {
              EAnnotation eAnnotation = eAttribute.getEAnnotation(VARIABLE_ANNOTATION_SOURCE);
              if (eAnnotation != null)
              {
                if (getAttributeRule(eAttribute, true) == null)
                {
                  // Determine if there exists an actual instance that really needs the rule.
                  String attributeName = ExtendedMetaData.INSTANCE.getName(eAttribute);
                  for (SetupTask setupTask : instances.get(eAttribute.getEContainingClass()))
                  {
                    // If there is an instance with an empty value.
                    Object value = setupTask.eGet(eAttribute);
                    if (value == null || "".equals(value))
                    {
                      // If that instance has an ID and hence will create an implied variable and that variable name isn't already defined by an existing
                      // context variable.
                      String id = setupTask.getID();
                      if (!StringUtil.isEmpty(id) && !keys.contains(id + "." + attributeName))
                      {
                        EMap<String, String> details = eAnnotation.getDetails();

                        // TODO class name/attribute name pairs might not be unique.
                        String variableName = "@<id>." + eClass.getName() + "." + attributeName;

                        VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
                        variable.setName(variableName);
                        variable.setType(VariableType.get(details.get("type")));
                        variable.setLabel(details.get("label"));
                        variable.setDescription(details.get("description"));
                        variable.eAdapters().add(RULE_VARIABLE_ADAPTER);
                        for (EAnnotation subAnnotation : eAnnotation.getEAnnotations())
                        {
                          if ("Choice".equals(subAnnotation.getSource()))
                          {
                            EMap<String, String> subDetails = subAnnotation.getDetails();

                            VariableChoice choice = SetupFactory.eINSTANCE.createVariableChoice();
                            choice.setValue(subDetails.get("value"));
                            choice.setLabel(subDetails.get("label"));

                            variable.getChoices().add(choice);
                          }
                        }

                        unresolvedVariables.add(variable);
                        ruleAttributes.put(variable, eAttribute);
                      }
                    }

                    break;
                  }
                }
              }
            }
          }
        }
      }

      if (!unresolvedVariables.isEmpty())
      {
        // 1.2.1. Prompt new rules and store them in User scope
        SetupPrompter prompter = getPrompter();
        if (!prompter.promptVariables(Collections.singletonList(this)))
        {
          throw new OperationCanceledException();
        }

        recordRules(attributeRules, false);
      }
    }

    if (!setupTasks.isEmpty())
    {
      Map<SetupTask, SetupTask> substitutions = getSubstitutions(setupTasks);

      // Shorten the paths through the substitutions map
      Map<SetupTask, SetupTask> directSubstitutions = new HashMap<SetupTask, SetupTask>(substitutions);
      for (Map.Entry<SetupTask, SetupTask> entry : directSubstitutions.entrySet())
      {
        SetupTask task = entry.getValue();

        for (;;)
        {
          SetupTask overridingTask = directSubstitutions.get(task);
          if (overridingTask == null)
          {
            break;
          }

          entry.setValue(overridingTask);
          task = overridingTask;
        }
      }

      if (!firstPhase)
      {
        // Perform override merging.
        Map<SetupTask, SetupTask> overrides = new HashMap<SetupTask, SetupTask>();
        for (Map.Entry<SetupTask, SetupTask> entry : substitutions.entrySet())
        {
          SetupTask overriddenSetupTask = entry.getKey();
          SetupTask overridingSetupTask = entry.getValue();
          overrides.put(overriddenSetupTask, overriddenSetupTask);

          overridingSetupTask.overrideFor(overriddenSetupTask);
        }

        for (int i = 0, size = setupTasks.size(); i < size; ++i)
        {
          SetupTask setupTask = setupTasks.get(i);
          for (int j = i + 1; j < size; ++j)
          {
            SetupTask otherSetupTask = setupTasks.get(j);
            if (EcoreUtil.equals(setupTask, otherSetupTask))
            {
              directSubstitutions.put(otherSetupTask, setupTask);
              overrides.put(setupTask, otherSetupTask);
              setupTask.overrideFor(otherSetupTask);
            }
          }
        }

        for (ListIterator<SetupTask> it = setupTasks.listIterator(); it.hasNext();)
        {
          SetupTask setupTask = it.next();
          if (directSubstitutions.containsKey(setupTask))
          {
            it.remove();
          }
          else
          {
            for (Iterator<SetupTask> it2 = setupTask.getPredecessors().iterator(); it2.hasNext();)
            {
              SetupTask predecessor = it2.next();
              if (directSubstitutions.containsKey(predecessor))
              {
                it.set(overrides.get(predecessor));
              }
            }

            for (Iterator<SetupTask> it2 = setupTask.getSuccessors().iterator(); it2.hasNext();)
            {
              SetupTask successor = it2.next();
              if (directSubstitutions.containsKey(successor))
              {
                it.set(overrides.get(successor));
              }
            }
          }
        }
      }
      else
      {
        // 2.2. Create copy based on overrides
        copySetup(stream, setupTasks, substitutions, directSubstitutions);

        // 2.4. Build variable map in the context
        Set<String> explicitKeys = new HashSet<String>();
        for (SetupTask setupTask : setupTasks)
        {
          if (setupTask instanceof VariableTask)
          {
            VariableTask contextVariableTask = (VariableTask)setupTask;

            String name = contextVariableTask.getName();
            explicitKeys.add(name);
          }
        }

        // 2.3. Create implied variables for annotated task attributes
        for (ListIterator<SetupTask> it = setupTasks.listIterator(); it.hasNext();)
        {
          SetupTask setupTask = it.next();

          String id = setupTask.getID();
          if (!StringUtil.isEmpty(id))
          {
            EClass eClass = setupTask.eClass();
            for (EAttribute eAttribute : eClass.getEAllAttributes())
            {
              if (eAttribute != SetupPackage.Literals.SETUP_TASK__ID && !eAttribute.isMany() && eAttribute.getEType().getInstanceClass() == String.class)
              {
                String variableName = id + "." + ExtendedMetaData.INSTANCE.getName(eAttribute);
                String value = (String)setupTask.eGet(eAttribute);
                if (explicitKeys.contains(variableName))
                {
                  if (StringUtil.isEmpty(value))
                  {
                    EAnnotation variableAnnotation = eAttribute.getEAnnotation(VARIABLE_ANNOTATION_SOURCE);
                    if (variableAnnotation != null)
                    {
                      setupTask.eSet(eAttribute, "${" + variableName + "}");
                    }
                  }
                }
                else
                {
                  VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
                  variable.setName(variableName);

                  EObject eContainer = setupTask.eContainer();
                  EReference eContainmentFeature = setupTask.eContainmentFeature();

                  @SuppressWarnings("unchecked")
                  EList<SetupTask> list = (EList<SetupTask>)eContainer.eGet(eContainmentFeature);
                  list.add(variable);

                  if (StringUtil.isEmpty(value))
                  {
                    EAnnotation variableAnnotation = eAttribute.getEAnnotation(VARIABLE_ANNOTATION_SOURCE);
                    if (variableAnnotation != null)
                    {
                      ruleBasedAttributes.put(variable, eAttribute);
                      createImpliedVariable(setupTask, eAttribute, variableAnnotation, variable);
                      setupTask.eSet(eAttribute, "${" + variableName + "}");
                    }
                    else
                    {
                      variable.setValue(value);
                    }
                  }
                  else
                  {
                    variable.setValue(value);
                  }

                  it.add(variable);

                  for (EAnnotation ruleVariableAnnotation : eAttribute.getEAnnotations())
                  {
                    if (RULE_VARIABLE_ANNOTATION_SOURCE.equals(ruleVariableAnnotation.getSource()))
                    {
                      EMap<String, String> details = ruleVariableAnnotation.getDetails();

                      VariableTask ruleVariable = SetupFactory.eINSTANCE.createVariableTask();
                      ruleVariable.setName(details.get("name"));
                      ruleVariable.setStorePromptedValue("true".equals(details.get("storePromptedValue")));

                      createImpliedVariable(setupTask, null, ruleVariableAnnotation, ruleVariable);
                      it.add(ruleVariable);
                    }
                  }
                }
              }
            }
          }
        }

        // 2.4. Build variable map in the context
        Set<String> keys = new HashSet<String>();
        for (SetupTask setupTask : setupTasks)
        {
          if (setupTask instanceof VariableTask)
          {
            VariableTask contextVariableTask = (VariableTask)setupTask;

            String name = contextVariableTask.getName();
            keys.add(name);

            String value = contextVariableTask.getValue();
            put(name, value);
            allVariables.put(name, contextVariableTask);
          }
        }

        expandVariableKeys(keys);

        // 2.8. Expand task attributes in sito
        expandStrings(setupTasks);

        flattenPredecessorsAndSuccessors(setupTasks);
        propagateRestrictionsAndFollows(setupTasks);
      }

      reorderSetupTasks(setupTasks);

      // Filter out the tasks that aren't triggered.
      if (trigger != null)
      {
        for (Iterator<SetupTask> it = setupTasks.iterator(); it.hasNext();)
        {
          if (!it.next().getTriggers().contains(trigger))
          {
            it.remove();
          }
        }
      }

      for (Iterator<SetupTask> it = setupTasks.iterator(); it.hasNext();)
      {
        SetupTask setupTask = it.next();
        setupTask.consolidate();
        if (setupTask instanceof VariableTask)
        {
          VariableTask contextVariableTask = (VariableTask)setupTask;
          if (!unresolvedVariables.contains(contextVariableTask))
          {
            resolvedVariables.add(contextVariableTask);
          }

          it.remove();
        }
      }
    }
  }

  private void recordRules(List<AttributeRule> attributeRules, boolean remove)
  {
    for (Iterator<VariableTask> it = unresolvedVariables.iterator(); it.hasNext();)
    {
      VariableTask variable = it.next();
      String value = variable.getValue();
      if (value != null)
      {
        String variableName = variable.getName();
        for (Map.Entry<VariableTask, EAttribute> entry : ruleAttributes.entrySet())
        {
          if (variableName.equals(entry.getKey().getName()))
          {
            URI uri = getAttributeURI(entry.getValue());

            AttributeRule attributeRule = null;
            for (AttributeRule existingAttributeRule : attributeRules)
            {
              if (uri.equals(existingAttributeRule.getAttributeURI()))
              {
                attributeRule = existingAttributeRule;
              }
            }

            if (attributeRule == null)
            {
              attributeRule = SetupFactory.eINSTANCE.createAttributeRule();
              attributeRule.setAttributeURI(uri);
            }

            attributeRule.setValue(value);
            attributeRules.add(attributeRule);

            if (remove)
            {
              it.remove();
            }

            break;
          }
        }
      }
    }
  }

  private void createImpliedVariable(SetupTask setupTask, EAttribute eAttribute, EAnnotation eAnnotation, VariableTask variable)
  {
    EMap<String, String> details = eAnnotation.getDetails();

    variable.setType(VariableType.get(details.get("type")));
    variable.setLabel(details.get("label"));
    variable.setDescription(details.get("description"));

    if (eAttribute != null)
    {
      AttributeRule attributeRule = getAttributeRule(eAttribute, false);
      if (attributeRule != null)
      {
        String attributeExpandedValue = expandAttributeReferences(setupTask, attributeRule.getValue());
        variable.setValue(attributeExpandedValue);

        // We must remember this applied rule in the preferences restricted to this workspace.
        appliedRuleVariables.add(variable);

        return;
      }
    }

    // Handle variable choices
    for (EAnnotation subAnnotation : eAnnotation.getEAnnotations())
    {
      if ("Choice".equals(subAnnotation.getSource()))
      {
        EMap<String, String> subDetails = subAnnotation.getDetails();

        VariableChoice choice = SetupFactory.eINSTANCE.createVariableChoice();
        String subValue = subDetails.get("value");
        subValue = expandAttributeReferences(setupTask, subValue);

        choice.setValue(subValue);
        choice.setLabel(subDetails.get("label"));

        variable.getChoices().add(choice);
      }
    }
  }

  private String expandAttributeReferences(SetupTask setupTask, String value)
  {
    EClass eClass = setupTask.eClass();
    Pattern pattern = Pattern.compile("@[\\p{Alpha}_][\\p{Alnum}_]*");
    Matcher matcher = pattern.matcher(value);

    StringBuilder builder = new StringBuilder();
    int index = 0;
    while (matcher.find())
    {
      builder.append(value, index, matcher.start());
      String key = matcher.group().substring(1);
      EStructuralFeature feature = eClass.getEStructuralFeature(key);
      if (feature == null)
      {
        feature = ExtendedMetaData.INSTANCE.getAttribute(eClass, null, key);
        if (feature == null)
        {
          throw new IllegalStateException("Attribute reference can not be resolved: " + key);
        }
      }

      Object featureValue = setupTask.eGet(feature);
      builder.append(featureValue);
      index = matcher.end();
    }

    builder.append(value, index, value.length());
    return builder.toString();
  }

  private AttributeRule getAttributeRule(EAttribute eAttribute, boolean userOnly)
  {
    URI attributeURI = getAttributeURI(eAttribute);
    User user = getUser();
    AttributeRule attributeRule = userOnly ? null : getAttributeRule(attributeURI, attributeRules);
    if (attributeRule == null)
    {
      attributeRule = getAttributeRule(attributeURI, user.getAttributeRules());
    }

    return attributeRule;
  }

  private AttributeRule getAttributeRule(URI attributeURI, List<AttributeRule> attributeRules)
  {
    for (AttributeRule attributeRule : attributeRules)
    {
      if (attributeURI.equals(attributeRule.getAttributeURI()))
      {
        return attributeRule;
      }
    }

    return null;
  }

  private URI getAttributeURI(EAttribute eAttribute)
  {
    EClass eClass = eAttribute.getEContainingClass();
    EPackage ePackage = eClass.getEPackage();
    URI uri = URI.createURI(ePackage.getNsURI()).appendFragment("//" + eClass.getName() + "/" + eAttribute.getName());
    return uri;
  }

  public EList<SetupTask> getTriggeredSetupTasks()
  {
    return triggeredSetupTasks;
  }

  public File getInstallationLocation()
  {
    for (SetupTask setupTask : triggeredSetupTasks)
    {
      if (setupTask instanceof InstallationTask)
      {
        return new File(((InstallationTask)setupTask).getLocation());
      }
    }

    return null;
  }

  public File getWorkspaceLocation()
  {
    for (SetupTask setupTask : triggeredSetupTasks)
    {
      if (setupTask instanceof WorkspaceTask)
      {
        return new File(((WorkspaceTask)setupTask).getLocation());
      }
    }

    return null;
  }

  public EList<SetupTask> getSetupTasks(Stream stream, Trigger trigger)
  {
    User user = getUser();
    Installation installation = getInstallation();
    Workspace workspace = getWorkspace();
    ProductVersion productVersion = installation.getProductVersion();

    EList<SetupTask> result = new BasicEList<SetupTask>();
    if (!productVersion.eIsProxy())
    {
      List<Scope> configurableItems = new ArrayList<Scope>();
      List<SetupTaskContainer> setupTaskContainers = new ArrayList<SetupTaskContainer>();

      Product product = productVersion.getProduct();
      configurableItems.add(product);
      setupTaskContainers.add(product);

      ProductCatalog productCatalog = product.getProductCatalog();
      configurableItems.add(productCatalog);
      setupTaskContainers.add(0, productCatalog);

      configurableItems.add(productVersion);
      setupTaskContainers.add(productVersion);

      if (stream != null)
      {
        Project project = stream.getProject();
        ProjectCatalog projectCatalog = project.getProjectCatalog();

        for (; project != null; project = project.getParentProject())
        {
          configurableItems.add(project);
          setupTaskContainers.add(3, project);
        }

        if (projectCatalog != null)
        {
          configurableItems.add(projectCatalog);
          setupTaskContainers.add(3, projectCatalog);
        }

        configurableItems.add(stream);
        setupTaskContainers.add(stream);
      }

      configurableItems.add(installation);
      setupTaskContainers.add(installation);

      if (workspace != null)
      {
        configurableItems.add(workspace);
        setupTaskContainers.add(workspace);
      }

      setupTaskContainers.add(user);

      for (SetupTaskContainer setupTaskContainer : setupTaskContainers)
      {
        getSetupTasks(trigger, result, configurableItems, setupTaskContainer);
      }
    }

    return result;
  }

  private void getSetupTasks(Trigger trigger, EList<SetupTask> setupTasks, List<Scope> configurableItems, SetupTaskContainer setupTaskContainer)
  {
    for (SetupTask setupTask : setupTaskContainer.getSetupTasks())
    {
      if (setupTask.isDisabled())
      {
        continue;
      }

      if (trigger != null && !setupTask.getTriggers().contains(trigger))
      {
        continue;
      }

      EList<Scope> restrictions = setupTask.getRestrictions();
      if (!configurableItems.containsAll(restrictions))
      {
        continue;
      }

      if (setupTask instanceof SetupTaskContainer)
      {
        SetupTaskContainer container = (SetupTaskContainer)setupTask;
        getSetupTasks(trigger, setupTasks, configurableItems, container);
      }
      else
      {
        setupTasks.add(setupTask);
      }
    }
  }

  public EList<SetupTask> initNeededSetupTasks() throws Exception
  {
    if (neededSetupTasks == null)
    {
      neededSetupTasks = new BasicEList<SetupTask>();

      if (!undeclaredVariables.isEmpty())
      {
        throw new RuntimeException("Missing variables for " + undeclaredVariables);
      }

      if (!unresolvedVariables.isEmpty())
      {
        throw new RuntimeException("Unresolved variables for " + unresolvedVariables);
      }

      if (triggeredSetupTasks != null)
      {
        for (Iterator<SetupTask> it = triggeredSetupTasks.iterator(); it.hasNext();)
        {
          SetupTask setupTask = it.next();

          try
          {
            if (setupTask.isNeeded(this))
            {
              neededSetupTasks.add(setupTask);
            }
          }
          catch (NoClassDefFoundError ex)
          {
            // Don't perform tasks that can't load their enabling dependencies
            SetupCorePlugin.INSTANCE.log(ex);
          }
        }
      }
    }

    return neededSetupTasks;
  }

  public EList<SetupTask> getNeededTasks()
  {
    return neededSetupTasks;
  }

  public Map<EObject, EObject> getCopyMap()
  {
    return copyMap;
  }

  public boolean isCanceled()
  {
    if (canceled)
    {
      return true;
    }

    if (progress != null)
    {
      return progress.isCanceled();
    }

    return false;
  }

  public void setCanceled(boolean canceled)
  {
    this.canceled = canceled;
  }

  public void task(SetupTask setupTask)
  {
    progress.task(setupTask);
  }

  public void log(Throwable t)
  {
    log(SetupCorePlugin.toString(t), false);
  }

  public void log(IStatus status)
  {
    log(SetupCorePlugin.toString(status), false);
  }

  public void log(String line)
  {
    log(line, true);
  }

  public void log(String line, boolean filter)
  {
    if (progress != null)
    {
      if (logMessageBuffer != null)
      {
        for (String value : logMessageBuffer)
        {
          doLog(value, filter);
        }

        logMessageBuffer = null;
      }

      doLog(line, filter);
    }
    else
    {
      if (logMessageBuffer == null)
      {
        logMessageBuffer = new ArrayList<String>();
      }

      logMessageBuffer.add(line);
    }
  }

  private void doLog(String line, boolean filter)
  {
    if (filter)
    {
      line = logFilter.filter(line);
    }

    if (line == null)
    {
      return;
    }

    try
    {
      PrintStream logStream = getLogStream();
      logStream.println("[" + DATE_TIME.format(new Date()) + "] " + line);
      logStream.flush();
    }
    catch (Exception ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
    }

    progress.log(line, filter);
  }

  private PrintStream getLogStream()
  {
    if (logStream == null)
    {
      try
      {
        File logFile = new File(SetupContext.SETUP_LOG_URI.toFileString());
        logFile.getParentFile().mkdirs();

        FileOutputStream out = new FileOutputStream(logFile, true);
        logStream = new PrintStream(out);
      }
      catch (FileNotFoundException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    return logStream;
  }

  public VariableTask getRuleVariable(VariableTask variable)
  {
    EAttribute eAttribute = ruleBasedAttributes.get(variable);
    if (eAttribute != null)
    {
      for (Map.Entry<VariableTask, EAttribute> entry : ruleAttributes.entrySet())
      {
        if (entry.getValue() == eAttribute)
        {
          return entry.getKey();
        }
      }
    }

    return null;
  }

  public boolean isRuleBased(VariableTask variable)
  {
    return ruleBasedAttributes.containsKey(variable);
  }

  public List<VariableTask> getUnresolvedVariables()
  {
    return unresolvedVariables;
  }

  public Map<VariableTask, EAttribute> getRuleAttributes()
  {
    return ruleAttributes;
  }

  public List<VariableTask> getAppliedRuleVariables()
  {
    return appliedRuleVariables;
  }

  public List<VariableTask> getResolvedVariables()
  {
    return resolvedVariables;
  }

  public Set<String> getUndeclaredVariables()
  {
    return undeclaredVariables;
  }

  private void expandStrings(EList<SetupTask> setupTasks)
  {
    Set<String> keys = new LinkedHashSet<String>();
    for (SetupTask setupTask : setupTasks)
    {
      expandVariableTaskValue(keys, setupTask);
    }

    for (SetupTask setupTask : setupTasks)
    {
      expand(keys, setupTask);
      for (Iterator<EObject> it = setupTask.eAllContents(); it.hasNext();)
      {
        expand(keys, it.next());
      }
    }

    if (!unresolvedSettings.isEmpty())
    {
      for (String key : keys)
      {
        boolean found = false;
        for (SetupTask setupTask : setupTasks)
        {
          if (setupTask instanceof VariableTask)
          {
            VariableTask contextVariableTask = (VariableTask)setupTask;
            if (key.equals(contextVariableTask.getName()))
            {
              contextVariableTask.setDefault(expandString(contextVariableTask.getDefault()));
              unresolvedVariables.add(contextVariableTask);
              found = true;
              break;
            }
          }
        }

        if (!found)
        {
          undeclaredVariables.add(key);
        }
      }
    }
  }

  public String expandString(String string)
  {
    return expandString(string, false);
  }

  public String expandString(String string, boolean secure)
  {
    return expandString(string, null, secure);
  }

  protected String expandString(String string, Set<String> keys)
  {
    return expandString(string, keys, false);
  }

  private String expandString(String string, Set<String> keys, boolean secure)
  {
    if (string == null)
    {
      return null;
    }

    StringBuilder result = new StringBuilder();
    int previous = 0;
    boolean unresolved = false;
    for (Matcher matcher = STRING_EXPANSION_PATTERN.matcher(string); matcher.find();)
    {
      result.append(string.substring(previous, matcher.start()));
      String key = matcher.group(1);
      if ("$".equals(key))
      {
        result.append('$');
      }
      else
      {
        key = matcher.group(2);
        String suffix = "";

        int prefixIndex = key.indexOf('/');
        if (prefixIndex != -1)
        {
          suffix = key.substring(prefixIndex);
          key = key.substring(0, prefixIndex);
          if (NEEDS_PATH_SEPARATOR_CONVERSION)
          {
            suffix = suffix.replace('/', File.separatorChar);
          }
        }

        boolean isUnexpanded = false;
        VariableTask variableTask = allVariables.get(key);
        if (variableTask != null)
        {
          for (Setting setting : unresolvedSettings)
          {
            if (setting.getEObject() == variableTask && setting.getEStructuralFeature() == SetupPackage.Literals.VARIABLE_TASK__VALUE)
            {
              isUnexpanded = true;
            }
          }
        }

        String value = isUnexpanded ? null : lookup(key);
        if (value == null)
        {
          value = isUnexpanded ? null : lookupSecurely(key); // If the value is in secure store, don't prompt for it

          if (value == null || !secure)
          {
            if (value == null && keys != null)
            {
              unresolved = true;

              if (!isUnexpanded)
              {
                keys.add(key);
              }
            }
            else if (!unresolved)
            {
              result.append(matcher.group());
            }
          }

          if (!secure)
          {
            value = null;
          }
        }

        if (value != null)
        {
          String filters = matcher.group(4);
          if (filters != null)
          {
            for (String filterName : filters.split("\\|"))
            {
              value = filter(value, filterName);
            }
          }

          if (!unresolved)
          {
            result.append(value);
            result.append(suffix);
          }
        }
      }

      previous = matcher.end();
    }

    if (unresolved)
    {
      return null;
    }

    result.append(string.substring(previous));
    return result.toString();
  }

  public Set<String> getVariables(String string)
  {
    if (string == null)
    {
      return null;
    }

    Set<String> result = new HashSet<String>();
    for (Matcher matcher = STRING_EXPANSION_PATTERN.matcher(string); matcher.find();)
    {
      String key = matcher.group(1);
      if (!"$".equals(key))
      {
        key = matcher.group(2);
        int prefixIndex = key.indexOf('/');
        if (prefixIndex != -1)
        {
          key = key.substring(0, prefixIndex);
        }
      }

      result.add(key);
    }

    return result;
  }

  private void propagateRestrictionsAndFollows(EList<SetupTask> setupTasks)
  {
    for (SetupTask setupTask : setupTasks)
    {
      EList<Scope> restrictions = setupTask.getRestrictions();
      for (EObject eContainer = setupTask.eContainer(); eContainer instanceof SetupTask; eContainer = eContainer.eContainer())
      {
        restrictions.addAll(((SetupTask)eContainer).getRestrictions());
      }

      EList<SetupTask> preceders = setupTask.getPredecessors();
      for (EObject eContainer = setupTask.eContainer(); eContainer instanceof SetupTask; eContainer = eContainer.eContainer())
      {
        preceders.addAll(((SetupTask)eContainer).getPredecessors());
      }
    }
  }

  private void flattenPredecessorsAndSuccessors(EList<SetupTask> setupTasks)
  {
    for (SetupTask setupTask : setupTasks)
    {
      for (ListIterator<SetupTask> it = setupTask.getPredecessors().listIterator(); it.hasNext();)
      {
        SetupTask predecessor = it.next();
        if (predecessor instanceof SetupTaskContainer)
        {
          it.remove();
          for (SetupTask expandedPrecessor : ((SetupTaskContainer)predecessor).getSetupTasks())
          {
            it.add(expandedPrecessor);
            it.previous();
          }
        }
      }

      for (ListIterator<SetupTask> it = setupTask.getSuccessors().listIterator(); it.hasNext();)
      {
        SetupTask successor = it.next();
        if (successor instanceof SetupTaskContainer)
        {
          it.remove();
          for (SetupTask expandedSuccessor : ((SetupTaskContainer)successor).getSetupTasks())
          {
            it.add(expandedSuccessor);
            it.previous();
          }
        }
      }
    }
  }

  private CompoundTask findOrCreate(AdapterFactoryItemDelegator itemDelegator, Scope configurableItem, EList<SetupTask> setupTasks)
  {
    EObject eContainer = configurableItem.eContainer();
    if (eContainer instanceof Scope)
    {
      CompoundTask compoundSetupTask = findOrCreate(itemDelegator, (Scope)eContainer, setupTasks);
      setupTasks = compoundSetupTask.getSetupTasks();
    }

    CompoundTask compoundSetupTask = find(configurableItem, setupTasks);
    if (compoundSetupTask == null)
    {
      compoundSetupTask = SetupFactory.eINSTANCE.createCompoundTask();
      compoundSetupTask.setName(itemDelegator.getText(configurableItem));
      compoundSetupTask.getRestrictions().add(configurableItem);

      setupTasks.add(compoundSetupTask);
    }

    return compoundSetupTask;
  }

  private CompoundTask find(Scope configurableItem, EList<SetupTask> setupTasks)
  {
    LOOP: for (SetupTask setupTask : setupTasks)
    {
      if (setupTask instanceof CompoundTask)
      {
        CompoundTask compoundSetupTask = (CompoundTask)setupTask;
        List<Scope> restrictions = ((InternalEList<Scope>)compoundSetupTask.getRestrictions()).basicList();
        URI uri = EcoreUtil.getURI(configurableItem);
        boolean found = false;
        for (Scope restriction : restrictions)
        {
          URI otherURI = EcoreUtil.getURI(restriction);
          if (!otherURI.equals(uri))
          {
            continue LOOP;
          }

          found = true;
        }

        if (found)
        {
          return compoundSetupTask;
        }

        compoundSetupTask = find(configurableItem, compoundSetupTask.getSetupTasks());
        if (compoundSetupTask != null)
        {
          return compoundSetupTask;
        }
      }
    }

    return null;
  }

  public void resolveSettings()
  {
    // Do this before expanding any more strings.
    List<Setting> unresolvedSettings = new ArrayList<EStructuralFeature.Setting>(this.unresolvedSettings);
    this.unresolvedSettings.clear();
    Set<String> keys = new HashSet<String>();
    for (VariableTask unspecifiedVariable : unresolvedVariables)
    {
      String name = unspecifiedVariable.getName();
      keys.add(name);

      String value = unspecifiedVariable.getValue();
      put(name, value);
    }

    for (EStructuralFeature.Setting setting : unresolvedSettings)
    {
      if (setting.getEStructuralFeature() == SetupPackage.Literals.VARIABLE_TASK__VALUE)
      {
        VariableTask variable = (VariableTask)setting.getEObject();
        String name = variable.getName();
        keys.add(name);

        String value = variable.getValue();
        put(name, value);
      }
    }

    expandVariableKeys(keys);

    for (EStructuralFeature.Setting setting : unresolvedSettings)
    {
      EStructuralFeature eStructuralFeature = setting.getEStructuralFeature();
      if (eStructuralFeature.isMany())
      {
        @SuppressWarnings("unchecked")
        List<String> values = (List<String>)setting.get(false);
        for (ListIterator<String> it = values.listIterator(); it.hasNext();)
        {
          it.set(expandString(it.next()));
        }
      }
      else
      {
        String value = (String)setting.get(false);
        String expandedString = expandString(value);
        setting.set(expandedString);
        if (eStructuralFeature == SetupPackage.Literals.VARIABLE_TASK__VALUE)
        {
          put(((VariableTask)setting.getEObject()).getName(), expandedString);
        }
      }
    }
  }

  private void expandVariableKeys(Set<String> keys)
  {
    Map<String, Set<String>> variables = new HashMap<String, Set<String>>();
    for (Map.Entry<Object, Object> entry : getMap().entrySet())
    {
      Object entryKey = entry.getKey();
      if (keys.contains(entryKey))
      {
        String key = (String)entryKey;
        Object entryValue = entry.getValue();
        if (entryValue == null)
        {
          VariableTask variable = allVariables.get(key);
          if (variable != null)
          {
            SetupPrompter prompter = getPrompter();
            String value = prompter.getValue(variable);
            if (value != null)
            {
              variable.setValue(value);
              Set<String> valueVariables = getVariables(value);
              variables.put(key, valueVariables);

              unresolvedVariables.add(variable);
              if (!valueVariables.isEmpty())
              {
                unresolvedSettings.add(((InternalEObject)variable).eSetting(SetupPackage.Literals.VARIABLE_TASK__VALUE));
              }
            }
          }
        }
        else if (entryKey instanceof String)
        {
          String value = entryValue.toString();
          variables.put(key, getVariables(value));
        }
      }
    }

    EList<Map.Entry<String, Set<String>>> orderedVariables = reorderVariables(variables);
    for (Map.Entry<String, Set<String>> entry : orderedVariables)
    {
      String key = entry.getKey();
      Object object = get(key);
      if (object != null)
      {
        String value = expandString(object.toString());
        put(key, value);
      }
    }
  }

  public void recordVariables(User user)
  {
    recordRules(user.getAttributeRules(), true);

    AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);
    EList<SetupTask> userSetupTasks = user.getSetupTasks();
    if (!unresolvedVariables.isEmpty())
    {
      applyUnresolvedVariables(user, unresolvedVariables, userSetupTasks, itemDelegator);
    }

    if (!appliedRuleVariables.isEmpty())
    {
      List<VariableTask> productCatalogScopedVariables = new ArrayList<VariableTask>();
      List<VariableTask> projectCatalogScopedVariables = new ArrayList<VariableTask>();
      EList<SetupTask> workspaceScopeTasks = null;
      EList<SetupTask> installationScopeTasks = null;
      for (VariableTask unspecifiedVariable : appliedRuleVariables)
      {
        for (EObject container = unspecifiedVariable.eContainer(); container != null; container = container.eContainer())
        {
          if (container instanceof Scope)
          {
            Scope scope = (Scope)container;
            switch (scope.getType())
            {
              case STREAM:
              case PROJECT:
              case PROJECT_CATALOG:
              case WORKSPACE:
              {
                Workspace workspace = getWorkspace();
                if (workspaceScopeTasks == null)
                {
                  workspaceScopeTasks = findOrCreate(itemDelegator, workspace, userSetupTasks).getSetupTasks();
                }
                projectCatalogScopedVariables.add(unspecifiedVariable);
                break;
              }

              case PRODUCT_VERSION:
              case PRODUCT:
              case PRODUCT_CATALOG:
              case INSTALLATION:
              {
                Installation installation = getInstallation();
                if (installationScopeTasks == null)
                {
                  installationScopeTasks = findOrCreate(itemDelegator, installation, userSetupTasks).getSetupTasks();
                }
                productCatalogScopedVariables.add(unspecifiedVariable);

                break;
              }

              case USER:
              {
                Workspace workspace = getWorkspace();
                if (workspace != null)
                {
                  if (workspaceScopeTasks == null)
                  {
                    workspaceScopeTasks = findOrCreate(itemDelegator, workspace, userSetupTasks).getSetupTasks();
                  }
                  projectCatalogScopedVariables.add(unspecifiedVariable);
                }

                Installation installation = getInstallation();
                if (installationScopeTasks == null)
                {
                  installationScopeTasks = findOrCreate(itemDelegator, installation, userSetupTasks).getSetupTasks();
                }
                productCatalogScopedVariables.add(unspecifiedVariable);

                break;
              }
            }

            break;
          }
        }
      }

      applyUnresolvedVariables(user, productCatalogScopedVariables, installationScopeTasks, itemDelegator);
      applyUnresolvedVariables(user, projectCatalogScopedVariables, workspaceScopeTasks, itemDelegator);
    }
  }

  private void applyUnresolvedVariables(User user, Collection<VariableTask> variables, EList<SetupTask> rootTasks, AdapterFactoryItemDelegator itemDelegator)
  {
    LOOP: for (VariableTask unspecifiedVariable : variables)
    {
      if (unspecifiedVariable.isStorePromptedValue())
      {
        String name = unspecifiedVariable.getName();
        String value = unspecifiedVariable.getValue();
        if (value != null)
        {
          // Save passwords to the secure storage
          if (unspecifiedVariable.getType() == VariableType.PASSWORD)
          {
            saveSecurePreference(name, value);
          }
          else
          {
            EList<SetupTask> targetSetupTasks = rootTasks;
            if (unspecifiedVariable.eContainer() != null)
            {
              for (EObject container = unspecifiedVariable.eContainer(); container != null; container = container.eContainer())
              {
                if (container instanceof Scope)
                {
                  targetSetupTasks = findOrCreate(itemDelegator, (Scope)container, rootTasks).getSetupTasks();
                  break;
                }
              }
            }

            // This happens in the multi-stream case where each perform wants to add setup-restricted tasks for the same variable.
            for (SetupTask setupTask : targetSetupTasks)
            {
              if (setupTask instanceof VariableTask)
              {
                VariableTask variable = (VariableTask)setupTask;
                if (name.equals(variable.getName()))
                {
                  variable.setValue(value);
                  continue LOOP;
                }
              }
            }

            VariableTask userPreference = SetupFactory.eINSTANCE.createVariableTask();
            userPreference.setName(name);
            userPreference.setValue(value); // Unexpanded value!

            targetSetupTasks.add(userPreference);
          }
        }
      }
    }
  }

  private void expandVariableTaskValue(Set<String> keys, EObject eObject)
  {
    if (eObject instanceof VariableTask)
    {
      VariableTask variableTask = (VariableTask)eObject;

      String value = variableTask.getValue();
      if (value != null)
      {
        String newValue = expandString(value, keys);
        if (newValue == null)
        {
          unresolvedSettings.add(((InternalEObject)eObject).eSetting(SetupPackage.Literals.VARIABLE_TASK__VALUE));
        }
        else if (!value.equals(newValue))
        {
          variableTask.setValue(newValue);
        }
      }
    }
  }

  private void expand(Set<String> keys, EObject eObject)
  {
    EClass eClass = eObject.eClass();
    for (EAttribute attribute : eClass.getEAllAttributes())
    {
      if (attribute.isChangeable() && attribute.getEAttributeType().getInstanceClassName() == "java.lang.String"
          && attribute.getEAnnotation("http://www.eclipse.org/oomph/setup/NoExpand") == null)
      {
        if (attribute.isMany())
        {
          @SuppressWarnings("unchecked")
          List<String> values = (List<String>)eObject.eGet(attribute);
          List<String> newValues = new ArrayList<String>();
          boolean failed = false;
          for (String value : values)
          {
            String newValue = expandString(value, keys);
            if (newValue == null)
            {
              if (!failed)
              {
                unresolvedSettings.add(((InternalEObject)eObject).eSetting(attribute));
                failed = true;
              }
            }
            else
            {
              newValues.add(newValue);
            }
          }

          if (!failed)
          {
            eObject.eSet(attribute, newValues);
          }
        }
        else
        {
          String value = (String)eObject.eGet(attribute);
          if (value != null)
          {
            String newValue = expandString(value, keys);
            if (newValue == null)
            {
              unresolvedSettings.add(((InternalEObject)eObject).eSetting(attribute));
            }
            else if (!value.equals(newValue))
            {
              eObject.eSet(attribute, newValue);
            }
          }
        }
      }
    }
  }

  private void performEclipseIniTask(boolean vm, String option, String value) throws Exception
  {
    EclipseIniTask task = SetupFactory.eINSTANCE.createEclipseIniTask();
    task.setVm(vm);
    task.setOption(option);
    task.setValue(value);
    performTask(task);
  }

  private void performTask(SetupTask task) throws Exception
  {
    if (task.isNeeded(this))
    {
      task.perform(this);
    }
  }

  public void perform() throws Exception
  {
    performTriggeredSetupTasks();

    if (getTrigger() == Trigger.BOOTSTRAP)
    {
      performEclipseIniTask(true, "-D" + SetupProperties.PROP_SETUP, "=true");
      performEclipseIniTask(true, "-D" + SetupProperties.PROP_UPDATE_URL, "=" + redirect(URI.createURI((String)get(SetupProperties.PROP_UPDATE_URL))));

      URI indexURI = SetupContext.INDEX_SETUP_URI;
      URI redirectedURI = redirect(indexURI);
      if (!redirectedURI.equals(indexURI))
      {
        URI baseURI = indexURI.trimSegments(1).appendSegment("");
        URI redirectedBaseURI = redirect(baseURI);
        if (!redirectedBaseURI.equals(baseURI))
        {
          URI baseBaseURI = baseURI.trimSegments(1).appendSegment("");
          URI redirectedBaseBaseURI = redirect(baseBaseURI);
          if (!redirectedBaseBaseURI.equals(baseBaseURI))
          {
            performEclipseIniTask(true, "-D" + SetupProperties.PROP_REDIRECTION_BASE + "index.redirection", "=" + baseBaseURI + "->" + redirectedBaseBaseURI);
          }
          else
          {
            performEclipseIniTask(true, "-D" + SetupProperties.PROP_REDIRECTION_BASE + "index.redirection", "=" + baseURI + "->" + redirectedBaseURI);
          }
        }
        else
        {
          performEclipseIniTask(true, "-D" + SetupProperties.PROP_REDIRECTION_BASE + "index.redirection", "=" + indexURI + "->" + redirectedURI);
        }
      }

      if (REMOTE_DEBUG)
      {
        performEclipseIniTask(true, "-D" + SetupProperties.PROP_SETUP_REMOTE_DEBUG, "=true");
        performEclipseIniTask(true, "-Xdebug", "");
        performEclipseIniTask(true, "-Xrunjdwp", ":transport=dt_socket,server=y,suspend=n,address=8123");
      }

      String[] networkPreferences = new String[] { ".settings", "org.eclipse.core.net.prefs" };
      URI sourceLocation = SetupContext.CONFIGURATION_LOCATION_URI.appendSegments(networkPreferences);
      if (getURIConverter().exists(sourceLocation, null))
      {
        URI targetURI = URI.createFileURI(new File(getProductLocation(), "configuration").toString()).appendSegments(networkPreferences);

        ResourceCopyTask resourceCopyTask = SetupFactory.eINSTANCE.createResourceCopyTask();
        resourceCopyTask.setSourceURL(sourceLocation.toString());
        resourceCopyTask.setTargetURL(targetURI.toString());
        performTask(resourceCopyTask);
      }
    }
  }

  private void performTriggeredSetupTasks() throws Exception
  {
    initNeededSetupTasks();
    if (!neededSetupTasks.isEmpty())
    {
      performNeededSetupTasks();
    }
  }

  public void performNeededSetupTasks() throws Exception
  {
    setPerforming(true);

    if (getTrigger() == Trigger.BOOTSTRAP)
    {
      doPerformNeededSetupTasks();
    }
    else
    {
      if (CommonPlugin.IS_RESOURCES_BUNDLE_AVAILABLE)
      {
        WorkspaceUtil.performNeededSetupTasks(this);
      }
      else
      {
        doPerformNeededSetupTasks();
      }
    }
  }

  private void doPerformNeededSetupTasks() throws Exception
  {
    Boolean autoBuilding = null;

    try
    {
      Trigger trigger = getTrigger();
      if (trigger != Trigger.BOOTSTRAP)
      {
        autoBuilding = disableAutoBuilding();
      }

      for (SetupTask neededTask : neededSetupTasks)
      {
        checkCancelation();

        // Once we're past all the installation priority tasks that might cause restart reasons and there are restart reasons, stop performing.
        if (trigger != Trigger.BOOTSTRAP && neededTask.getPriority() >= SetupTask.PRIORITY_CONFIGURATION && !getRestartReasons().isEmpty())
        {
          break;
        }

        task(neededTask);
        log("Performing setup task " + getLabel(neededTask));

        try
        {
          neededTask.perform(this);
          neededTask.dispose();
        }
        catch (NoClassDefFoundError ex)
        {
          log(ex);
        }
      }
    }
    catch (Exception ex)
    {
      log(ex);
      throw ex;
    }
    finally
    {
      if (autoBuilding != null)
      {
        restoreAutoBuilding(autoBuilding);
      }

      try
      {
        PrintStream logStream = getLogStream();
        logStream.println();
        logStream.println();
        logStream.println();
        logStream.println();
        IOUtil.closeSilent(logStream);
      }
      catch (Exception ex)
      {
        SetupCorePlugin.INSTANCE.log(ex);
      }
    }
  }

  private Map<SetupTask, SetupTask> getSubstitutions(EList<SetupTask> setupTasks)
  {
    Map<Object, SetupTask> overrides = new HashMap<Object, SetupTask>();
    Map<SetupTask, SetupTask> substitutions = new LinkedHashMap<SetupTask, SetupTask>();

    for (SetupTask setupTask : setupTasks)
    {
      Object overrideToken = setupTask.getOverrideToken();
      SetupTask overriddenTask = overrides.put(overrideToken, setupTask);
      if (overriddenTask != null)
      {
        substitutions.put(overriddenTask, setupTask);
      }
    }

    return substitutions;
  }

  private void gather(Set<EObject> roots, Set<Scope> scopesToCopy, EObject eObject)
  {
    EObject result = eObject;
    for (EObject parent = eObject; parent != null; parent = parent.eContainer())
    {
      if (parent instanceof Scope)
      {
        if (!scopesToCopy.add((Scope)parent))
        {
          return;
        }
      }

      result = parent;
    }

    roots.add(result);
  }

  private void copySetup(Stream stream, EList<SetupTask> setupTasks, Map<SetupTask, SetupTask> substitutions, Map<SetupTask, SetupTask> directSubstitutions)
  {
    Set<EObject> roots = new LinkedHashSet<EObject>();
    final Set<Scope> scopesToCopy = new LinkedHashSet<Scope>();

    Workspace originalWorkspace = getWorkspace();
    if (originalWorkspace != null)
    {
      scopesToCopy.add(originalWorkspace);
      roots.add(originalWorkspace);

      if (stream != null)
      {
        gather(roots, scopesToCopy, stream);
      }
    }

    User originalPreferences = getUser();
    if (originalPreferences != null)
    {
      scopesToCopy.add(originalPreferences);
      roots.add(originalPreferences);
    }

    Installation originalInstallation = getInstallation();
    if (originalInstallation != null)
    {
      scopesToCopy.add(originalInstallation);
      roots.add(originalInstallation);

      for (EObject eObject : originalInstallation.eCrossReferences())
      {
        gather(roots, scopesToCopy, eObject);
      }
    }

    for (SetupTask setupTask : setupTasks)
    {
      gather(roots, scopesToCopy, setupTask);
    }

    EcoreUtil.Copier copier = new EcoreUtil.Copier(true, stream == null)
    {
      private static final long serialVersionUID = 1L;

      @Override
      public <T> Collection<T> copyAll(Collection<? extends T> eObjects)
      {
        Collection<T> result = new ArrayList<T>(eObjects.size());
        for (Object object : eObjects)
        {
          @SuppressWarnings("unchecked")
          T t = (T)copy((EObject)object);
          if (t != null)
          {
            result.add(t);
          }
        }
        return result;
      }

      @Override
      protected EObject createCopy(EObject eObject)
      {
        if (eObject instanceof Scope && !scopesToCopy.contains(eObject))
        {
          return null;
        }

        return super.createCopy(eObject);
      }
    };

    copier.copyAll(roots);

    // Determine all the copied objects for which the original object is directly contained in a resource.
    // For each such resource, create a copy of that resource.
    Map<Resource, Resource> resourceCopies = new HashMap<Resource, Resource>();

    @SuppressWarnings("unchecked")
    Set<InternalEObject> originals = (Set<InternalEObject>)(Set<?>)copier.keySet();
    for (InternalEObject original : originals)
    {
      Internal resource = original.eDirectResource();
      if (resource != null)
      {
        Resource newResource = resourceCopies.get(resource);
        if (newResource == null)
        {
          URI uri = resource.getURI();
          ResourceSet resourceSet = resource.getResourceSet();
          Registry resourceFactoryRegistry = resourceSet == null ? Resource.Factory.Registry.INSTANCE : resourceSet.getResourceFactoryRegistry();
          newResource = resourceFactoryRegistry.getFactory(uri).createResource(uri);
          resourceCopies.put(resource, newResource);
        }
      }
    }

    // For each original resource, ensure that the copied resource contains either the corresponding copies or
    // a placeholder object.
    for (Map.Entry<Resource, Resource> entry : resourceCopies.entrySet())
    {
      Resource originalResource = entry.getKey();
      Resource copyResource = entry.getValue();
      EList<EObject> copyResourceContents = copyResource.getContents();
      for (EObject eObject : originalResource.getContents())
      {
        EObject copy = copier.get(eObject);
        if (copy == null)
        {
          copy = EcoreFactory.eINSTANCE.createEObject();
        }

        copyResourceContents.add(copy);
      }
    }

    // Must determine mapping from original setup's references (ProductVersion and Streams) to their copies currently in the copier.

    Map<URI, EObject> originalCrossReferences = new HashMap<URI, EObject>();
    if (originalWorkspace != null)
    {
      for (EObject eObject : originalWorkspace.eCrossReferences())
      {
        originalCrossReferences.put(EcoreUtil.getURI(eObject), eObject);
      }
    }

    for (EObject copiedObject : new ArrayList<EObject>(copier.values()))
    {
      URI uri = EcoreUtil.getURI(copiedObject);
      EObject originalObject = originalCrossReferences.get(uri);
      if (originalObject != null)
      {
        copier.put(originalObject, copiedObject);
      }
    }

    HashMap<EObject, EObject> originalCopier = new HashMap<EObject, EObject>(copier);
    for (Map.Entry<SetupTask, SetupTask> entry : directSubstitutions.entrySet())
    {
      SetupTask overriddenTask = entry.getKey();
      SetupTask overridingTask = entry.getValue();

      EObject copy = copier.get(overridingTask);
      copier.put(overriddenTask, copy == null ? overridingTask : copy);
    }

    copyMap = copier;

    copier.copyReferences();

    // Perform override merging.
    for (Map.Entry<SetupTask, SetupTask> entry : substitutions.entrySet())
    {
      SetupTask originalOverriddenSetupTask = entry.getKey();
      SetupTask overriddenSetupTask = (SetupTask)originalCopier.get(originalOverriddenSetupTask);
      // For synthesized tasks, there is no copy, only the original.
      if (overriddenSetupTask == null)
      {
        overriddenSetupTask = originalOverriddenSetupTask;
      }

      SetupTask originalOverridingSetupTask = entry.getValue();
      SetupTask overridingSetupTask = (SetupTask)originalCopier.get(originalOverridingSetupTask);
      // For synthesized tasks, there is no copy, only the original.
      if (overridingSetupTask == null)
      {
        overridingSetupTask = originalOverridingSetupTask;
      }

      overridingSetupTask.overrideFor(overriddenSetupTask);
    }

    for (ListIterator<SetupTask> it = setupTasks.listIterator(); it.hasNext();)
    {
      SetupTask setupTask = it.next();
      if (directSubstitutions.containsKey(setupTask))
      {
        it.remove();
      }
      else
      {
        SetupTask copy = (SetupTask)copier.get(setupTask);
        it.set(copy);
      }
    }

    setSetupContext(SetupContext.create((Installation)copier.get(originalInstallation), (Workspace)copier.get(originalWorkspace),
        (User)copier.get(originalPreferences)));
  }

  private EList<Map.Entry<String, Set<String>>> reorderVariables(final Map<String, Set<String>> variables)
  {
    EList<Map.Entry<String, Set<String>>> list = new BasicEList<Map.Entry<String, Set<String>>>(variables.entrySet());

    EMFUtil.reorder(list, new EMFUtil.DependencyProvider<Map.Entry<String, Set<String>>>()
    {
      public Collection<Map.Entry<String, Set<String>>> getDependencies(Map.Entry<String, Set<String>> variable)
      {
        Collection<Map.Entry<String, Set<String>>> result = new ArrayList<Map.Entry<String, Set<String>>>();
        for (String key : variable.getValue())
        {
          for (Map.Entry<String, Set<String>> entry : variables.entrySet())
          {
            if (entry.getKey().equals(key))
            {
              result.add(entry);
            }
          }
        }

        return result;
      }
    });

    return list;
  }

  private void reorderSetupTasks(EList<SetupTask> setupTasks)
  {
    ECollections.sort(setupTasks, new Comparator<SetupTask>()
    {
      public int compare(SetupTask setupTask1, SetupTask setupTask2)
      {
        return setupTask1.getPriority() - setupTask2.getPriority();
      }
    });

    final Map<SetupTask, Set<SetupTask>> dependencies = new HashMap<SetupTask, Set<SetupTask>>();
    for (SetupTask setupTask : setupTasks)
    {
      addAll(dependencies, setupTask, setupTask.getPredecessors());

      for (SetupTask successor : setupTask.getSuccessors())
      {
        add(dependencies, successor, setupTask);
      }
    }

    EMFUtil.reorder(setupTasks, new EMFUtil.DependencyProvider<SetupTask>()
    {
      public Collection<SetupTask> getDependencies(SetupTask setupTask)
      {
        return dependencies.get(setupTask);
      }
    });
  }

  private String getLabel(SetupTask setupTask)
  {
    IItemLabelProvider labelProvider = (IItemLabelProvider)adapterFactory.adapt(setupTask, IItemLabelProvider.class);
    String type;

    try
    {
      Method getTypeTextMethod = ReflectUtil.getMethod(labelProvider.getClass(), "getTypeText", Object.class);
      getTypeTextMethod.setAccessible(true);
      type = getTypeTextMethod.invoke(labelProvider, setupTask).toString();
    }
    catch (Exception ex)
    {
      type = setupTask.eClass().getName();
    }

    String label = labelProvider.getText(setupTask);
    return label.startsWith(type) ? label : type + " " + label;
  }

  /**
   * Used in IDE.
   */
  public static SetupTaskPerformer createForIDE(ResourceSet resourceSet, SetupPrompter prompter, Trigger trigger) throws Exception
  {
    return create(resourceSet.getURIConverter(), prompter, trigger, SetupContext.create(resourceSet), false);
  }

  /**
   * Used in installer and IDE.
   */
  public static SetupTaskPerformer create(URIConverter uriConverter, final SetupPrompter prompter, Trigger trigger, SetupContext setupContext,
      boolean fullPrompt) throws Exception
  {
    List<SetupTaskPerformer> performers = new ArrayList<SetupTaskPerformer>();
    boolean needsPrompt = false;

    List<VariableTask> allAppliedRuleVariables = new ArrayList<VariableTask>();
    List<VariableTask> allUnresolvedVariables = new ArrayList<VariableTask>();
    Map<VariableTask, EAttribute> allRuleAttributes = new HashMap<VariableTask, EAttribute>();

    Workspace workspace = setupContext.getWorkspace();
    if (workspace == null || workspace.getStreams().isEmpty())
    {
      SetupTaskPerformer performer = new SetupTaskPerformer(uriConverter, prompter, null, setupContext, (Stream)null);
      performers.add(performer);
      allAppliedRuleVariables.addAll(performer.getAppliedRuleVariables());
      Set<String> undeclaredVariables = performer.getUndeclaredVariables();
      if (!undeclaredVariables.isEmpty())
      {
        throw new RuntimeException("Missing variables for " + undeclaredVariables);
      }

      if (performer.getUndeclaredVariables().isEmpty() && !performer.getUnresolvedVariables().isEmpty())
      {
        needsPrompt = true;
      }
    }
    else
    {
      for (Stream stream : workspace.getStreams())
      {
        SetupTaskPerformer performer = new SetupTaskPerformer(uriConverter, prompter, null, setupContext, stream);
        Set<String> undeclaredVariables = performer.getUndeclaredVariables();
        final Set<VariableTask> demandCreatedUnresolvedVariables = new LinkedHashSet<VariableTask>();
        if (!undeclaredVariables.isEmpty())
        {
          List<VariableTask> unresolvedVariables = performer.getUnresolvedVariables();
          for (String variableName : undeclaredVariables)
          {
            VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
            variable.setName(variableName);
            variable.setLabel(variableName + " (undeclared)");
            variable.setStorePromptedValue(false);
            unresolvedVariables.add(variable);
            demandCreatedUnresolvedVariables.add(variable);
          }

          undeclaredVariables.clear();
        }

        if (fullPrompt)
        {
          final SetupTaskPerformer partialPromptPerformer = performer;
          SetupContext fullPromptContext = SetupContext.create(setupContext.getInstallation(), setupContext.getWorkspace());
          SetupPrompter fullPrompter = new SetupPrompter()
          {
            private boolean first = true;

            public UserCallback getUserCallback()
            {
              return prompter.getUserCallback();
            }

            public String getValue(VariableTask variable)
            {
              if (!first)
              {
                return prompter.getValue(variable);
              }

              return null;
            }

            public boolean promptVariables(List<? extends SetupTaskContext> performers)
            {
              for (SetupTaskContext context : performers)
              {
                SetupTaskPerformer promptedPerformer = (SetupTaskPerformer)context;
                Map<VariableTask, EAttribute> ruleAttributes = promptedPerformer.getRuleAttributes();
                for (VariableTask variable : promptedPerformer.getUnresolvedVariables())
                {
                  EAttribute eAttribute = ruleAttributes.get(variable);
                  if (ruleAttributes.keySet().contains(variable))
                  {
                    AttributeRule attributeRule = partialPromptPerformer.getAttributeRule(eAttribute, true);
                    if (attributeRule != null)
                    {
                      variable.setValue(attributeRule.getValue());
                    }
                  }
                  else
                  {
                    Object value = partialPromptPerformer.get(variable.getName());
                    if (value instanceof String)
                    {
                      variable.setValue(value.toString());
                    }
                  }
                }

                promptedPerformer.getUnresolvedVariables().addAll(demandCreatedUnresolvedVariables);
              }

              first = false;
              return true;
            }
          };

          SetupTaskPerformer fullPromptPerformer = new SetupTaskPerformer(uriConverter, fullPrompter, null, fullPromptContext, stream);
          fullPrompter.promptVariables(Collections.singletonList(fullPromptPerformer));
          performer = fullPromptPerformer;
        }

        allAppliedRuleVariables.addAll(performer.getAppliedRuleVariables());
        allUnresolvedVariables.addAll(performer.getUnresolvedVariables());
        allRuleAttributes.putAll(performer.getRuleAttributes());
        performers.add(performer);

        if (!performer.getUnresolvedVariables().isEmpty())
        {
          needsPrompt = true;
        }
      }
    }

    if (needsPrompt)
    {
      if (!prompter.promptVariables(performers))
      {
        return null;
      }

      for (SetupTaskPerformer setupTaskPerformer : performers)
      {
        setupTaskPerformer.resolveSettings();
      }
    }

    // All variables have been expanded, no unresolved variables remain.
    // We need a single performer for all streams.
    // The per-stream performers from above have triggered task lists that must be composed into a single setup for multiple streams.

    EList<SetupTask> setupTasks = new BasicEList<SetupTask>();
    for (SetupTaskPerformer performer : performers)
    {
      setupTasks.addAll(performer.getTriggeredSetupTasks());
    }

    SetupTaskPerformer composedPerformer = new SetupTaskPerformer(uriConverter, prompter, trigger, setupContext, setupTasks);
    composedPerformer.getAppliedRuleVariables().addAll(allAppliedRuleVariables);
    composedPerformer.getUnresolvedVariables().addAll(allUnresolvedVariables);
    composedPerformer.getRuleAttributes().putAll(allRuleAttributes);
    File workspaceLocation = composedPerformer.getWorkspaceLocation();
    if (workspaceLocation != null)
    {
      File workspaceSetupLocation = new File(workspaceLocation, ".metadata/.plugins/org.eclipse.oomph.setup/workspace.setup");
      URI workspaceURI = URI.createFileURI(workspaceSetupLocation.toString());
      for (SetupTaskPerformer performer : performers)
      {
        performer.getWorkspace().eResource().setURI(workspaceURI);
      }
    }

    File eclipseDir = composedPerformer.getProductLocation();
    if (eclipseDir != null)
    {
      File installationLocation = new File(eclipseDir, "configuration/org.eclipse.oomph.setup/installation.setup");
      URI installationURI = URI.createFileURI(installationLocation.toString());
      for (SetupTaskPerformer performer : performers)
      {
        performer.getInstallation().eResource().setURI(installationURI);
      }
    }

    return composedPerformer;
  }

  public static ProgressLog getProgress()
  {
    return progress;
  }

  public static void setProgress(ProgressLog progress)
  {
    SetupTaskPerformer.progress = progress;
  }

  public static boolean disableAutoBuilding() throws CoreException
  {
    return CommonPlugin.IS_RESOURCES_BUNDLE_AVAILABLE && WorkspaceUtil.disableAutoBuilding();
  }

  public static void restoreAutoBuilding(boolean autoBuilding) throws CoreException
  {
    if (CommonPlugin.IS_RESOURCES_BUNDLE_AVAILABLE)
    {
      WorkspaceUtil.restoreAutoBuilding(autoBuilding);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class WorkspaceUtil
  {
    private static boolean disableAutoBuilding() throws CoreException
    {
      boolean autoBuilding = ResourcesPlugin.getWorkspace().isAutoBuilding();
      if (autoBuilding)
      {
        restoreAutoBuilding(false);
      }

      return autoBuilding;
    }

    private static void restoreAutoBuilding(boolean autoBuilding) throws CoreException
    {
      if (autoBuilding != ResourcesPlugin.getWorkspace().isAutoBuilding())
      {
        IWorkspaceDescription description = ResourcesPlugin.getWorkspace().getDescription();
        description.setAutoBuilding(autoBuilding);

        ResourcesPlugin.getWorkspace().setDescription(description);
      }
    }

    private static void performNeededSetupTasks(final SetupTaskPerformer performer) throws Exception
    {
      ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
      {
        public void run(IProgressMonitor monitor) throws CoreException
        {
          try
          {
            performer.doPerformNeededSetupTasks();
          }
          catch (Throwable t)
          {
            SetupCorePlugin.INSTANCE.coreException(t);
          }
        }
      }, null, IWorkspace.AVOID_UPDATE, null);
    }
  }
}
