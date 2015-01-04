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
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.EAnnotationConstants;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.RedirectionTask;
import org.eclipse.oomph.setup.ResourceCopyTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.ScopeType;
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
import org.eclipse.oomph.setup.impl.InstallationTaskImpl;
import org.eclipse.oomph.setup.internal.core.util.Authenticator;
import org.eclipse.oomph.setup.internal.core.util.SetupUtil;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.log.ProgressLogFilter;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.setup.util.StringExpander;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
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
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
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
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.metadata.VersionRange;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.Properties;
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

  private static final Map<String, ValueConverter> CONVERTERS = new HashMap<String, ValueConverter>();

  static
  {
    CONVERTERS.put("java.lang.String", new ValueConverter());
    CONVERTERS.put("org.eclipse.emf.common.util.URI", new URIValueConverter());
  }

  private static final SimpleDateFormat DATE_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private static final Pattern INSTALLABLE_UNIT_WITH_RANGE_PATTERN = Pattern.compile("([^\\[\\(]*)(.*)");

  private static Pattern ATTRIBUTE_REFERENCE_PATTERN = Pattern.compile("@[\\p{Alpha}_][\\p{Alnum}_]*");

  private ProgressLog progress;

  private boolean canceled;

  private EList<SetupTask> triggeredSetupTasks;

  private Map<EObject, EObject> copyMap;

  private EList<SetupTask> neededSetupTasks;

  private Set<Bundle> bundles = new HashSet<Bundle>();

  private List<String> logMessageBuffer;

  private PrintStream logStream;

  private ProgressLogFilter logFilter = new ProgressLogFilter();

  private List<EStructuralFeature.Setting> unresolvedSettings = new ArrayList<EStructuralFeature.Setting>();

  private List<VariableTask> passwordVariables = new ArrayList<VariableTask>();

  private Map<URI, String> passwords = new LinkedHashMap<URI, String>();

  private List<VariableTask> unresolvedVariables = new ArrayList<VariableTask>();

  private List<VariableTask> resolvedVariables = new ArrayList<VariableTask>();

  private List<VariableTask> appliedRuleVariables = new ArrayList<VariableTask>();

  private Map<String, VariableTask> allVariables = new LinkedHashMap<String, VariableTask>();

  private Set<String> undeclaredVariables = new LinkedHashSet<String>();

  private Map<VariableTask, EAttribute> ruleAttributes = new LinkedHashMap<VariableTask, EAttribute>();

  private Map<VariableTask, EAttribute> ruleBasedAttributes = new LinkedHashMap<VariableTask, EAttribute>();

  private List<AttributeRule> attributeRules = new ArrayList<AttributeRule>();

  private ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();

  private boolean hasSuccessfullyPerformed;

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

  public boolean hasSuccessfullyPerformed()
  {
    return hasSuccessfullyPerformed;
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
      triggeredSetupTasks = new BasicEList<SetupTask>(getSetupTasks(stream));
      bundles.add(SetupCorePlugin.INSTANCE.getBundle());

      // 1. Collect and flatten all tasks
      Set<EClass> eClasses = new LinkedHashSet<EClass>();
      Map<EClass, Set<SetupTask>> instances = new LinkedHashMap<EClass, Set<SetupTask>>();
      Set<String> keys = new LinkedHashSet<String>();
      for (SetupTask setupTask : triggeredSetupTasks)
      {
        try
        {
          Bundle bundle = FrameworkUtil.getBundle(setupTask.getClass());
          if (bundle != null)
          {
            bundles.add(bundle);
          }
        }
        catch (Throwable ex)
        {
          //$FALL-THROUGH$
        }

        EClass eClass = setupTask.eClass();
        CollectionUtil.add(instances, eClass, setupTask);
        eClasses.add(eClass);
        for (EClass eSuperType : eClass.getEAllSuperTypes())
        {
          if (SetupPackage.Literals.SETUP_TASK.isSuperTypeOf(eSuperType))
          {
            eClasses.add(eSuperType);
            CollectionUtil.add(instances, eSuperType, setupTask);
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
        EList<SetupTask> enablementTasks = createEnablementTasks(eClass, true);
        if (enablementTasks != null)
        {
          triggeredSetupTasks.addAll(0, enablementTasks);
        }

        for (EAnnotation eAnnotation : eClass.getEAnnotations())
        {
          String source = eAnnotation.getSource();
          if (EAnnotationConstants.ANNOTATION_VARIABLE.equals(source))
          {
            triggeredSetupTasks.add(0, createImpliedVariable(eAnnotation));
          }
        }

        if (user.eResource() != null)
        {
          // 1.2. Determine whether new rules need to be created
          for (EAttribute eAttribute : eClass.getEAttributes())
          {
            if (eAttribute.getEType().getInstanceClass() == String.class)
            {
              EAnnotation eAnnotation = eAttribute.getEAnnotation(EAnnotationConstants.ANNOTATION_VARIABLE);
              if (eAnnotation != null && eAttribute.getEAnnotation(EAnnotationConstants.ANNOTATION_RULE_VARIABLE) != null)
              {
                AttributeRule attributeRule = getAttributeRule(eAttribute, true);
                if (attributeRule == null)
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
                        String variableName = getAttributeRuleVariableName(eAttribute);

                        VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
                        annotateAttributeRuleVariable(variable, eAttribute);
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

      // 1.2.1. Prompt new rules and store them in User scope
      SetupPrompter prompter = getPrompter();
      prompter.promptVariables(Collections.singletonList(this));
      recordRules(attributeRules, false);
    }

    if (!triggeredSetupTasks.isEmpty())
    {
      Map<SetupTask, SetupTask> substitutions = getSubstitutions(triggeredSetupTasks);

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
          overrides.put(overriddenSetupTask, overridingSetupTask);
          overridingSetupTask.overrideFor(overriddenSetupTask);
        }

        for (int i = triggeredSetupTasks.size(); --i >= 0;)
        {
          SetupTask setupTask = triggeredSetupTasks.get(i);
          if (!directSubstitutions.containsKey(setupTask))
          {
            for (int j = i; --j >= 0;)
            {
              SetupTask otherSetupTask = triggeredSetupTasks.get(j);

              // We must ignore specific references that are bound to be different, but don't affect what the task actually does.
              EcoreUtil.EqualityHelper equalityHelper = new EcoreUtil.EqualityHelper()
              {
                private static final long serialVersionUID = 1L;

                @Override
                protected boolean haveEqualReference(EObject eObject1, EObject eObject2, EReference reference)
                {
                  if (reference == SetupPackage.Literals.SETUP_TASK__PREDECESSORS || reference == SetupPackage.Literals.SETUP_TASK__SUCCESSORS
                      || reference == SetupPackage.Literals.SETUP_TASK__RESTRICTIONS)
                  {
                    return true;
                  }

                  return super.haveEqualReference(eObject1, eObject2, reference);
                }
              };
              if (equalityHelper.equals(setupTask, otherSetupTask))
              {
                directSubstitutions.put(otherSetupTask, setupTask);
                overrides.put(setupTask, otherSetupTask);
                setupTask.overrideFor(otherSetupTask);
              }
            }
          }
        }

        EList<SetupTask> remainingSetupTasks = new UniqueEList.FastCompare<SetupTask>();
        for (SetupTask setupTask : triggeredSetupTasks)
        {
          SetupTask overridingSetupTask = directSubstitutions.get(setupTask);
          if (overridingSetupTask != null)
          {
            remainingSetupTasks.add(overridingSetupTask);
          }
          else
          {
            remainingSetupTasks.add(setupTask);
          }
        }

        // Replace predecessor references to refer to the direct substitution.
        for (SetupTask setupTask : remainingSetupTasks)
        {
          EList<SetupTask> predecessors = setupTask.getPredecessors();
          for (ListIterator<SetupTask> it = predecessors.listIterator(); it.hasNext();)
          {
            SetupTask predecessor = it.next();
            SetupTask overridingSetupTask = directSubstitutions.get(predecessor);
            if (overridingSetupTask != null)
            {
              if (predecessors.contains(overridingSetupTask) || overridingSetupTask.requires(predecessor))
              {
                it.remove();
              }
              else
              {
                it.set(overridingSetupTask);
              }
            }
          }
        }

        ECollections.setEList(triggeredSetupTasks, remainingSetupTasks);
      }
      else
      {
        // 2.2. Create copy based on overrides
        copySetup(stream, triggeredSetupTasks, substitutions, directSubstitutions);

        // 2.4. Build variable map in the context
        Map<String, VariableTask> explicitKeys = new HashMap<String, VariableTask>();
        for (SetupTask setupTask : triggeredSetupTasks)
        {
          if (setupTask instanceof VariableTask)
          {
            VariableTask variableTask = (VariableTask)setupTask;

            String name = variableTask.getName();
            explicitKeys.put(name, variableTask);
          }
        }

        // 2.3. Create implied variables for annotated task attributes
        for (ListIterator<SetupTask> it = triggeredSetupTasks.listIterator(); it.hasNext();)
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
                String variableReference = getVariableReference(variableName);
                if (explicitKeys.containsKey(variableName))
                {
                  if (StringUtil.isEmpty(value) || setupTask instanceof WorkspaceTask)
                  {
                    EAnnotation variableAnnotation = eAttribute.getEAnnotation(EAnnotationConstants.ANNOTATION_VARIABLE);
                    if (variableAnnotation != null)
                    {
                      setupTask.eSet(eAttribute, variableReference);
                    }
                  }
                }
                else
                {
                  VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
                  annotateImpliedVariable(variable, setupTask, eAttribute);
                  variable.setName(variableName);

                  EObject eContainer = setupTask.eContainer();
                  EReference eContainmentFeature = setupTask.eContainmentFeature();

                  @SuppressWarnings("unchecked")
                  EList<SetupTask> list = (EList<SetupTask>)eContainer.eGet(eContainmentFeature);
                  list.add(variable);

                  if (StringUtil.isEmpty(value))
                  {
                    EAnnotation variableAnnotation = eAttribute.getEAnnotation(EAnnotationConstants.ANNOTATION_VARIABLE);
                    if (variableAnnotation != null)
                    {
                      ruleBasedAttributes.put(variable, eAttribute);
                      populateImpliedVariable(setupTask, eAttribute, variableAnnotation, variable);
                      setupTask.eSet(eAttribute, variableReference);
                    }
                  }
                  else
                  {
                    EAnnotation variableAnnotation = eAttribute.getEAnnotation(EAnnotationConstants.ANNOTATION_VARIABLE);
                    if (variableAnnotation != null)
                    {
                      populateImpliedVariable(setupTask, null, variableAnnotation, variable);
                      setupTask.eSet(eAttribute, variableReference);
                    }

                    variable.setValue(value);
                  }

                  it.add(variable);
                  explicitKeys.put(variableName, variable);

                  for (EAnnotation ruleVariableAnnotation : eAttribute.getEAnnotations())
                  {
                    if (EAnnotationConstants.ANNOTATION_RULE_VARIABLE.equals(ruleVariableAnnotation.getSource()))
                    {
                      EMap<String, String> details = ruleVariableAnnotation.getDetails();

                      VariableTask ruleVariable = SetupFactory.eINSTANCE.createVariableTask();
                      annotateRuleVariable(ruleVariable, variable);
                      String ruleVariableName = details.get(EAnnotationConstants.KEY_NAME);
                      ruleVariable.setName(ruleVariableName);
                      ruleVariable.setStorageURI(BaseFactory.eINSTANCE.createURI(details.get(EAnnotationConstants.KEY_STORAGE_URI)));

                      populateImpliedVariable(setupTask, null, ruleVariableAnnotation, ruleVariable);
                      it.add(ruleVariable);
                      explicitKeys.put(ruleVariableName, ruleVariable);
                    }
                  }

                  // If the variable is a self reference.
                  EAnnotation variableAnnotation = eAttribute.getEAnnotation(EAnnotationConstants.ANNOTATION_VARIABLE);
                  if (variableAnnotation != null && variableReference.equals(variable.getValue()))
                  {
                    EMap<String, String> details = variableAnnotation.getDetails();
                    VariableTask explicitVariable = SetupFactory.eINSTANCE.createVariableTask();
                    String explicitVariableName = variableName + ".explicit";
                    explicitVariable.setName(explicitVariableName);
                    explicitVariable.setStorageURI(null);
                    explicitVariable.setType(VariableType.get(details.get(EAnnotationConstants.KEY_EXPLICIT_TYPE)));
                    explicitVariable.setLabel(expandAttributeReferences(setupTask, details.get(EAnnotationConstants.KEY_EXPLICIT_LABEL)));
                    explicitVariable.setDescription(expandAttributeReferences(setupTask, details.get(EAnnotationConstants.KEY_EXPLICIT_DESCRIPTION)));
                    it.add(explicitVariable);
                    explicitKeys.put(explicitVariableName, explicitVariable);
                    annotateRuleVariable(explicitVariable, variable);

                    variable.setValue(getVariableReference(explicitVariableName));
                  }
                }
              }
            }
          }
        }

        for (SetupTask setupTask : triggeredSetupTasks)
        {
          handleActiveAnnotations(setupTask, explicitKeys);
        }

        // 2.4. Build variable map in the context
        Set<String> keys = new LinkedHashSet<String>();
        boolean fullPromptUser = isFullPromptUser(user);
        VariableAdapter variableAdapter = new VariableAdapter(this);
        for (SetupTask setupTask : triggeredSetupTasks)
        {
          if (setupTask instanceof VariableTask)
          {
            VariableTask variable = (VariableTask)setupTask;
            variable.eAdapters().add(variableAdapter);

            String name = variable.getName();
            keys.add(name);

            String value = variable.getValue();

            if (variable.getType() == VariableType.PASSWORD)
            {
              passwordVariables.add(variable);
              if (StringUtil.isEmpty(value) && !fullPromptUser)
              {
                URI storageURI = getEffectiveStorage(variable);
                if (storageURI != null && PreferencesUtil.PREFERENCE_SCHEME.equals(storageURI.scheme()))
                {
                  URIConverter uriConverter = getURIConverter();
                  if (uriConverter.exists(storageURI, null))
                  {
                    try
                    {
                      Reader reader = ((URIConverter.ReadableInputStream)uriConverter.createInputStream(storageURI)).asReader();
                      StringBuilder result = new StringBuilder();
                      for (int character = reader.read(); character != -1; character = reader.read())
                      {
                        result.append((char)character);
                      }
                      reader.close();
                      value = PreferencesUtil.encrypt(result.toString());
                    }
                    catch (IOException ex)
                    {
                      SetupCorePlugin.INSTANCE.log(ex);
                    }
                  }
                }
              }
            }

            put(name, value);
            allVariables.put(name, variable);
          }
        }

        expandVariableKeys(keys);

        // 2.8. Expand task attributes in situ
        expandStrings(triggeredSetupTasks);

        flattenPredecessorsAndSuccessors(triggeredSetupTasks);
        propagateRestrictionsPredecessorsAndSuccessors(triggeredSetupTasks);
      }

      reorderSetupTasks(triggeredSetupTasks);

      // Filter out the tasks that aren't triggered.
      if (trigger != null)
      {
        for (Iterator<SetupTask> it = triggeredSetupTasks.iterator(); it.hasNext();)
        {
          if (!it.next().getTriggers().contains(trigger))
          {
            it.remove();
          }
        }
      }

      for (Iterator<SetupTask> it = triggeredSetupTasks.iterator(); it.hasNext();)
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

  private String getAttributeRuleVariableName(EAttribute eAttribute)
  {
    String attributeName = ExtendedMetaData.INSTANCE.getName(eAttribute);
    String variableName = "@<id>." + eAttribute.getEContainingClass() + "." + attributeName;
    return variableName;
  }

  private void handleActiveAnnotations(SetupTask setupTask, Map<String, VariableTask> explicitKeys)
  {
    for (Annotation annotation : setupTask.getAnnotations())
    {
      String source = annotation.getSource();
      if (AnnotationConstants.ANNOTATION_INHERITED_CHOICES.equals(source) && setupTask instanceof VariableTask)
      {
        VariableTask variableTask = (VariableTask)setupTask;
        EList<VariableChoice> choices = variableTask.getChoices();

        EMap<String, String> details = annotation.getDetails();
        String inherit = details.get(AnnotationConstants.KEY_INHERIT);
        if (inherit != null)
        {
          for (String variableName : inherit.trim().split("\\s"))
          {
            VariableTask referencedVariableTask = explicitKeys.get(variableName);
            if (referencedVariableTask != null)
            {
              for (VariableChoice variableChoice : referencedVariableTask.getChoices())
              {
                String value = variableChoice.getValue();
                String label = variableChoice.getLabel();
                for (Map.Entry<String, String> detail : annotation.getDetails().entrySet())
                {
                  String detailKey = detail.getKey();
                  String detailValue = detail.getValue();
                  if (detailKey != null && !AnnotationConstants.KEY_INHERIT.equals(detailKey) && detailValue != null)
                  {
                    String target = "@{" + detailKey + "}";
                    if (value != null)
                    {
                      value = value.replace(target, detailValue);
                    }

                    if (label != null)
                    {
                      label = label.replace(target, detailValue);
                    }
                  }
                }

                VariableChoice choice = SetupFactory.eINSTANCE.createVariableChoice();
                choice.setValue(value);
                choice.setLabel(label);
                choices.add(choice);
              }
            }
          }
        }
      }
      else if (AnnotationConstants.ANNOTATION_INDUCED_CHOICES.equals(source))
      {
        String id = setupTask.getID();
        if (id != null)
        {
          EMap<String, String> details = annotation.getDetails();
          String inherit = details.get(AnnotationConstants.KEY_INHERIT);
          String target = details.get(AnnotationConstants.KEY_TARGET);
          if (target != null && inherit != null)
          {
            EStructuralFeature eStructuralFeature = BaseUtil.getFeature(setupTask.eClass(), target);
            if (eStructuralFeature.getEType().getInstanceClass() == String.class)
            {
              VariableTask variableTask = explicitKeys.get(id + "." + target);
              if (variableTask != null)
              {
                EList<VariableChoice> targetChoices = variableTask.getChoices();
                if (!targetChoices.isEmpty())
                {
                  if (!StringUtil.isEmpty(variableTask.getValue()))
                  {
                    setupTask.eSet(eStructuralFeature, getVariableReference(variableTask.getName()));
                  }
                }
                else
                {
                  EList<VariableChoice> choices = targetChoices;
                  Map<String, String> substitutions = new LinkedHashMap<String, String>();
                  for (Map.Entry<String, String> detail : annotation.getDetails().entrySet())
                  {
                    String detailKey = detail.getKey();
                    String detailValue = detail.getValue();
                    if (detailKey != null && !AnnotationConstants.KEY_INHERIT.equals(detailKey) && !AnnotationConstants.KEY_TARGET.equals(detailKey)
                        && !AnnotationConstants.KEY_LABEL.equals(detailKey) && !AnnotationConstants.KEY_DESCRIPTION.equals(detailKey) && detailValue != null)
                    {
                      if (detailValue.startsWith("@"))
                      {
                        String featureName = detailValue.substring(1);
                        EStructuralFeature referencedEStructuralFeature = BaseUtil.getFeature(setupTask.eClass(), featureName);
                        if (referencedEStructuralFeature != null && referencedEStructuralFeature.getEType().getInstanceClass() == String.class
                            && !referencedEStructuralFeature.isMany())
                        {
                          Object value = setupTask.eGet(referencedEStructuralFeature);
                          if (value != null)
                          {
                            detailValue = value.toString();
                          }
                        }
                      }

                      substitutions.put("@{" + detailKey + "}", detailValue);
                    }
                  }

                  for (EAttribute eAttribute : setupTask.eClass().getEAllAttributes())
                  {
                    if (eAttribute.getEType().getInstanceClass() == String.class && !eAttribute.isMany())
                    {
                      String value = (String)setupTask.eGet(eAttribute);
                      if (!StringUtil.isEmpty(value))
                      {
                        substitutions.put("@{" + ExtendedMetaData.INSTANCE.getName(eAttribute) + "}", value);
                      }
                    }
                  }

                  String inheritedLabel = null;
                  String inheritedDescription = null;

                  for (String variableName : inherit.trim().split("\\s"))
                  {
                    VariableTask referencedVariableTask = explicitKeys.get(variableName);
                    if (referencedVariableTask != null)
                    {
                      if (inheritedLabel == null)
                      {
                        inheritedLabel = referencedVariableTask.getLabel();
                      }

                      if (inheritedDescription == null)
                      {
                        inheritedDescription = referencedVariableTask.getDescription();
                      }

                      for (VariableChoice variableChoice : referencedVariableTask.getChoices())
                      {
                        String value = variableChoice.getValue();
                        String label = variableChoice.getLabel();
                        for (Map.Entry<String, String> detail : substitutions.entrySet())
                        {
                          String detailKey = detail.getKey();
                          String detailValue = detail.getValue();
                          if (value != null)
                          {
                            value = value.replace(detailKey, detailValue);
                          }

                          if (label != null)
                          {
                            label = label.replace(detailKey, detailValue);
                          }
                        }

                        VariableChoice choice = SetupFactory.eINSTANCE.createVariableChoice();
                        choice.setValue(value);
                        choice.setLabel(label);
                        choices.add(choice);
                      }
                    }
                  }

                  if (ObjectUtil.equals(setupTask.eGet(eStructuralFeature), variableTask.getValue()))
                  {
                    String explicitLabel = details.get(AnnotationConstants.KEY_LABEL);
                    if (explicitLabel == null)
                    {
                      explicitLabel = inheritedLabel;
                    }

                    String explicitDescription = details.get(AnnotationConstants.KEY_DESCRIPTION);
                    if (explicitDescription == null)
                    {
                      explicitDescription = inheritedDescription;
                    }

                    variableTask.setValue(null);
                    variableTask.setLabel(explicitLabel);
                    variableTask.setDescription(explicitDescription);
                  }
                  setupTask.eSet(eStructuralFeature, getVariableReference(variableTask.getName()));
                }
              }
            }
          }
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
                break;
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

  private void annotateAttributeRuleVariable(VariableTask variable, EAttribute eAttribute)
  {
    annotate(variable, "AttributeRuleVariable", eAttribute);
  }

  public EAttribute getAttributeRuleVariableData(VariableTask variable)
  {
    Annotation annotation = variable.getAnnotation("AttributeRuleVariable");
    if (annotation != null)
    {
      return (EAttribute)annotation.getReferences().get(0);
    }

    return null;
  }

  private void annotateImpliedVariable(VariableTask variable, SetupTask setupTask, EAttribute eAttribute)
  {
    annotate(variable, "ImpliedVariable", setupTask, eAttribute);
  }

  public EStructuralFeature.Setting getImpliedVariableData(VariableTask variable)
  {
    Annotation annotation = variable.getAnnotation("ImpliedVariable");
    if (annotation != null)
    {
      EList<EObject> references = annotation.getReferences();
      InternalEObject setupTask = (InternalEObject)references.get(0);
      return setupTask.eSetting((EStructuralFeature)references.get(1));
    }

    return null;
  }

  private void annotateRuleVariable(VariableTask variable, VariableTask dependentVariable)
  {
    annotate(variable, "RuleVariable", dependentVariable);
  }

  public VariableTask getRuleVariableData(VariableTask variable)
  {
    Annotation annotation = variable.getAnnotation("RuleVariable");
    if (annotation != null)
    {
      return (VariableTask)annotation.getReferences().get(0);
    }

    return null;
  }

  private void annotate(ModelElement modelElement, String source, EObject... references)
  {
    Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
    annotation.setSource(source);
    annotation.getReferences().addAll(Arrays.asList(references));
    modelElement.getAnnotations().add(annotation);
  }

  private VariableTask createImpliedVariable(EAnnotation eAnnotation)
  {
    EMap<String, String> details = eAnnotation.getDetails();

    VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
    variable.setName(details.get(EAnnotationConstants.KEY_NAME));

    populateImpliedVariable(null, null, eAnnotation, variable);

    return variable;
  }

  private void populateImpliedVariable(SetupTask setupTask, EAttribute eAttribute, EAnnotation eAnnotation, VariableTask variable)
  {
    EMap<String, String> details = eAnnotation.getDetails();

    variable.setType(VariableType.get(details.get(EAnnotationConstants.KEY_TYPE)));
    variable.setLabel(expandAttributeReferences(setupTask, details.get(EAnnotationConstants.KEY_LABEL)));
    variable.setDescription(expandAttributeReferences(setupTask, details.get(EAnnotationConstants.KEY_DESCRIPTION)));

    // The storageURI remains the default unless there is an explicit key to specify it be null or whatever else is specified.
    if (details.containsKey(EAnnotationConstants.KEY_STORAGE_URI))
    {
      String storageURIValue = expandAttributeReferences(setupTask, details.get(EAnnotationConstants.KEY_STORAGE_URI));
      variable.setStorageURI(StringUtil.isEmpty(storageURIValue) ? null : URI.createURI(storageURIValue));
    }

    if (eAttribute != null)
    {
      AttributeRule attributeRule = getAttributeRule(eAttribute, false);
      if (attributeRule != null)
      {
        String value = attributeRule.getValue();
        VariableTask ruleVariable = getRuleVariable(variable);
        if (ruleVariable == null)
        {
          ruleVariable = SetupFactory.eINSTANCE.createVariableTask();
          ruleVariable.setName(getAttributeRuleVariableName(eAttribute));
        }

        VariableType explicitVariableType = VariableType.get(details.get(EAnnotationConstants.KEY_EXPLICIT_TYPE));
        if (explicitVariableType != null)
        {
          variable.setType(explicitVariableType);
        }

        String explicitLabel = details.get(EAnnotationConstants.KEY_EXPLICIT_LABEL);
        variable.setLabel(expandAttributeReferences(setupTask, explicitLabel));

        String explicitDescription = details.get(EAnnotationConstants.KEY_EXPLICIT_DESCRIPTION);
        variable.setDescription(expandAttributeReferences(setupTask, explicitDescription));

        String promptedValue = getPrompter().getValue(ruleVariable);
        if (promptedValue != null)
        {
          value = promptedValue;
        }

        String attributeExpandedValue = expandAttributeReferences(setupTask, value);
        variable.setValue(attributeExpandedValue);

        // We must remember this applied rule in the preferences restricted to this workspace.
        appliedRuleVariables.add(variable);

        return;
      }
    }

    // Handle variable choices
    for (EAnnotation subAnnotation : eAnnotation.getEAnnotations())
    {
      if (EAnnotationConstants.NESTED_ANNOTATION_CHOICE.equals(subAnnotation.getSource()))
      {
        EMap<String, String> subDetails = subAnnotation.getDetails();

        VariableChoice choice = SetupFactory.eINSTANCE.createVariableChoice();
        String subValue = expandAttributeReferences(setupTask, subDetails.get(EAnnotationConstants.KEY_VALUE));

        choice.setValue(subValue);
        choice.setLabel(subDetails.get(EAnnotationConstants.KEY_LABEL));

        variable.getChoices().add(choice);
      }
    }
  }

  private String expandAttributeReferences(SetupTask setupTask, String value)
  {
    if (setupTask == null || value == null)
    {
      return value;
    }

    EClass eClass = setupTask.eClass();
    Matcher matcher = ATTRIBUTE_REFERENCE_PATTERN.matcher(value);

    StringBuilder builder = new StringBuilder();
    int index = 0;
    for (; matcher.find(); index = matcher.end())
    {
      builder.append(value, index, matcher.start());
      String key = matcher.group().substring(1);
      EStructuralFeature feature = eClass.getEStructuralFeature(key);
      if (feature == null)
      {
        feature = BaseUtil.getFeature(eClass, key);
        if (feature == null)
        {
          builder.append('@');
          builder.append(key);

          continue;
        }
      }

      Object featureValue = setupTask.eGet(feature);
      builder.append(featureValue);
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

  public static URI getAttributeURI(EAttribute eAttribute)
  {
    EClass eClass = eAttribute.getEContainingClass();
    EPackage ePackage = eClass.getEPackage();
    URI uri = URI.createURI(ePackage.getNsURI()).appendFragment("//" + eClass.getName() + "/" + eAttribute.getName());
    return uri;
  }

  public Set<Bundle> getBundles()
  {
    return bundles;
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

  public EList<SetupTask> getSetupTasks(Stream stream)
  {
    User user = getUser();
    Installation installation = getInstallation();
    Workspace workspace = getWorkspace();
    ProductVersion productVersion = installation.getProductVersion();

    EList<SetupTask> result = new BasicEList<SetupTask>();
    if (productVersion != null && !productVersion.eIsProxy())
    {
      List<Scope> configurableItems = new ArrayList<Scope>();
      List<Scope> scopes = new ArrayList<Scope>();

      Product product = productVersion.getProduct();
      configurableItems.add(product);
      scopes.add(product);

      ProductCatalog productCatalog = product.getProductCatalog();
      configurableItems.add(productCatalog);
      scopes.add(0, productCatalog);

      configurableItems.add(productVersion);
      scopes.add(productVersion);

      if (stream != null)
      {
        Project project = stream.getProject();
        ProjectCatalog projectCatalog = project.getProjectCatalog();

        for (; project != null; project = project.getParentProject())
        {
          configurableItems.add(project);
          scopes.add(3, project);
        }

        if (projectCatalog != null)
        {
          configurableItems.add(projectCatalog);
          scopes.add(3, projectCatalog);
        }

        configurableItems.add(stream);
        scopes.add(stream);
      }

      configurableItems.add(installation);
      scopes.add(installation);

      if (workspace != null)
      {
        configurableItems.add(workspace);
        scopes.add(workspace);
      }

      scopes.add(user);

      String qualifier = null;

      for (Scope scope : scopes)
      {
        ScopeType type = scope.getType();
        String name = scope.getName();
        String label = scope.getLabel();
        if (label == null)
        {
          label = name;
        }

        String description = scope.getDescription();
        if (description == null)
        {
          description = label;
        }

        switch (type)
        {
          case PRODUCT_CATALOG:
          {
            generateScopeVariables(result, "product.catalog", qualifier, name, label, description);
            qualifier = name;
            break;
          }
          case PRODUCT:
          {
            generateScopeVariables(result, "product", qualifier, name, label, description);
            qualifier += "." + name;
            break;
          }
          case PRODUCT_VERSION:
          {
            generateScopeVariables(result, "product.version", qualifier, name, label, description);
            qualifier = null;
            break;
          }
          case PROJECT_CATALOG:
          {
            generateScopeVariables(result, "project.catalog", qualifier, name, label, description);
            qualifier = name;
            break;
          }
          case PROJECT:
          {
            generateScopeVariables(result, "project", qualifier, name, label, description);
            qualifier += "." + name;
            break;
          }
          case STREAM:
          {
            generateScopeVariables(result, "project.stream", qualifier, name, label, description);
            qualifier = null;
            break;
          }
          case INSTALLATION:
          {
            generateScopeVariables(result, "installation", qualifier, name, label, description);
            break;
          }
          case WORKSPACE:
          {
            generateScopeVariables(result, "workspace", qualifier, name, label, description);
            break;
          }
          case USER:
          {
            generateScopeVariables(result, "user", qualifier, name, label, description);
            break;
          }
        }

        getSetupTasks(result, configurableItems, scope);
      }
    }

    return result;
  }

  private void generateScopeVariables(EList<SetupTask> setupTasks, String type, String qualifier, String name, String label, String description)
  {
    setupTasks.add(createVariable(setupTasks, "scope." + type + ".name", name, null));

    if (qualifier != null)
    {
      setupTasks.add(createVariable(setupTasks, "scope." + type + ".name.qualified", qualifier + "." + name, null));
    }

    setupTasks.add(createVariable(setupTasks, "scope." + type + ".label", label, null));
    setupTasks.add(createVariable(setupTasks, "scope." + type + ".description", description, null));
  }

  private VariableTask createVariable(EList<SetupTask> setupTasks, String name, String value, String description)
  {
    VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
    variable.setName(name);
    variable.setValue(value);
    variable.setDescription(description);
    return variable;
  }

  private void getSetupTasks(EList<SetupTask> setupTasks, List<Scope> configurableItems, SetupTaskContainer setupTaskContainer)
  {
    for (SetupTask setupTask : setupTaskContainer.getSetupTasks())
    {
      if (setupTask.isDisabled())
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
        getSetupTasks(setupTasks, configurableItems, container);
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
          checkCancelation();

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

  public void setTerminating()
  {
    if (progress != null)
    {
      progress.setTerminating();
    }
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
      PrintStream logStream = getLogStream(true);
      logStream.println("[" + DATE_TIME.format(new Date()) + "] " + line);
      logStream.flush();
    }
    catch (Exception ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
    }

    progress.log(line, filter);
  }

  public PrintStream getLogStream()
  {
    return logStream;
  }

  private PrintStream getLogStream(boolean demandCreate)
  {
    if (logStream == null && demandCreate)
    {
      try
      {
        File productLocation = getProductLocation();
        String path = InstallationTaskImpl.CONFIGURATION_FOLDER_NAME + "/" + SetupContext.OOMPH_NODE + "/" + SetupContext.LOG_FILE_NAME;

        File logFile = new File(productLocation, path);
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

  public List<VariableTask> getPasswordVariables()
  {
    return passwordVariables;
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

  public void redirectTriggeredSetupTasks()
  {
    Map<URI, URI> uriMap = getURIConverter().getURIMap();
    for (Iterator<SetupTask> it = triggeredSetupTasks.iterator(); it.hasNext();)
    {
      SetupTask setupTask = it.next();
      if (setupTask instanceof RedirectionTask)
      {
        RedirectionTask redirectionTask = (RedirectionTask)setupTask;
        URI sourceURI = URI.createURI(redirectionTask.getSourceURL());
        URI targetURI = URI.createURI(redirectionTask.getTargetURL());

        uriMap.put(sourceURI, targetURI);
        it.remove();
      }
    }

    for (SetupTask setupTask : triggeredSetupTasks)
    {
      for (Iterator<EObject> it = EcoreUtil.getAllContents(setupTask, false); it.hasNext();)
      {
        redirectStrings(it.next());
      }
    }
  }

  private void redirectStrings(EObject eObject)
  {
    EClass eClass = eObject.eClass();

    for (EAttribute attribute : eClass.getEAllAttributes())
    {
      if (attribute.isChangeable() && attribute.getEAnnotation(EAnnotationConstants.ANNOTATION_REDIRECT) != null)
      {
        String instanceClassName = attribute.getEAttributeType().getInstanceClassName();
        ValueConverter valueConverter = CONVERTERS.get(instanceClassName);
        if (valueConverter != null)
        {
          if (attribute.isMany())
          {
            List<?> values = (List<?>)eObject.eGet(attribute);
            List<Object> newValues = new ArrayList<Object>();
            boolean changed = false;

            for (Object value : values)
            {
              String convertedValue = valueConverter.convertToString(value);
              String redirectedValue = redirect(convertedValue);
              if (!ObjectUtil.equals(convertedValue, redirectedValue))
              {
                changed = true;
              }

              newValues.add(valueConverter.createFromString(redirectedValue));
            }

            if (changed)
            {
              eObject.eSet(attribute, newValues);
            }
          }
          else
          {
            Object value = eObject.eGet(attribute);
            if (value != null)
            {
              String convertedValue = valueConverter.convertToString(value);
              String redirectedValue = redirect(convertedValue);
              if (!ObjectUtil.equals(convertedValue, redirectedValue))
              {
                eObject.eSet(attribute, valueConverter.createFromString(redirectedValue));
              }
            }
          }
        }
      }
    }
  }

  private void expandStrings(EList<SetupTask> setupTasks)
  {
    Set<String> keys = new LinkedHashSet<String>();
    for (SetupTask setupTask : setupTasks)
    {
      expandVariableTaskValue(keys, setupTask);
    }

    for (Iterator<EObject> it = EcoreUtil.getAllContents(setupTasks); it.hasNext();)
    {
      expand(keys, it.next());
    }

    handleFeatureSubstitutions(setupTasks);

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

  @Override
  protected String resolve(String key)
  {
    return lookup(key);
  }

  @Override
  protected boolean isUnexpanded(String key)
  {
    VariableTask variableTask = allVariables.get(key);
    if (variableTask != null)
    {
      for (Setting setting : unresolvedSettings)
      {
        if (setting.getEObject() == variableTask && setting.getEStructuralFeature() == SetupPackage.Literals.VARIABLE_TASK__VALUE)
        {
          return true;
        }
      }
    }

    return false;
  }

  public Set<String> getVariables(String string)
  {
    if (string == null)
    {
      return null;
    }

    Set<String> result = new LinkedHashSet<String>();
    for (Matcher matcher = StringExpander.STRING_EXPANSION_PATTERN.matcher(string); matcher.find();)
    {
      String key = matcher.group(1);
      if (!"$".equals(key))
      {
        key = matcher.group(2);
      }

      result.add(key);
    }

    return result;
  }

  private void propagateRestrictionsPredecessorsAndSuccessors(EList<SetupTask> setupTasks)
  {
    for (SetupTask setupTask : setupTasks)
    {
      EList<Scope> restrictions = setupTask.getRestrictions();
      for (EObject eContainer = setupTask.eContainer(); eContainer instanceof SetupTask; eContainer = eContainer.eContainer())
      {
        restrictions.addAll(((SetupTask)eContainer).getRestrictions());
      }

      EList<SetupTask> predecessors = setupTask.getPredecessors();
      for (EObject eContainer = setupTask.eContainer(); eContainer instanceof SetupTask; eContainer = eContainer.eContainer())
      {
        predecessors.addAll(((SetupTask)eContainer).getPredecessors());
      }

      EList<SetupTask> successors = setupTask.getSuccessors();
      for (EObject eContainer = setupTask.eContainer(); eContainer instanceof SetupTask; eContainer = eContainer.eContainer())
      {
        successors.addAll(((SetupTask)eContainer).getSuccessors());
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
    Set<String> keys = new LinkedHashSet<String>();
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
      ValueConverter valueConverter = CONVERTERS.get(eStructuralFeature.getEType().getInstanceClassName());
      if (eStructuralFeature.isMany())
      {
        @SuppressWarnings("unchecked")
        List<Object> values = (List<Object>)setting.get(false);
        for (ListIterator<Object> it = values.listIterator(); it.hasNext();)
        {
          it.set(valueConverter.createFromString(expandString(valueConverter.convertToString(it.next()))));
        }
      }
      else
      {
        Object value = setting.get(false);
        String expandedString = expandString(valueConverter.convertToString(value));
        setting.set(valueConverter.createFromString(expandedString));
        if (eStructuralFeature == SetupPackage.Literals.VARIABLE_TASK__VALUE)
        {
          put(((VariableTask)setting.getEObject()).getName(), expandedString);
        }
      }
    }

    handleFeatureSubstitutions(triggeredSetupTasks);
  }

  private void handleFeatureSubstitutions(Collection<? extends EObject> eObjects)
  {
    // Find all the feature substitution annotations.
    for (Iterator<EObject> it = EcoreUtil.getAllContents(eObjects); it.hasNext();)
    {
      InternalEObject eObject = (InternalEObject)it.next();
      if (eObject instanceof Annotation)
      {
        Annotation annotation = (Annotation)eObject;
        if (AnnotationConstants.ANNOTATION_FEATURE_SUBSTITUTION.equals(annotation.getSource()))
        {
          ModelElement modelElement = annotation.getModelElement();
          EClass eClass = modelElement.eClass();
          for (Map.Entry<String, String> detail : annotation.getDetails())
          {
            // Look for an attribute with the name of the detail's key.
            EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(detail.getKey());
            if (eStructuralFeature instanceof EAttribute)
            {
              try
              {
                // Convert the detail's value to a value of that attribute's type and replace it.
                modelElement.eSet(eStructuralFeature, EcoreUtil.createFromString(((EAttribute)eStructuralFeature).getEAttributeType(), detail.getValue()));
              }
              catch (RuntimeException ex)
              {
                // Ignore.
              }
            }
          }
        }
      }
    }
  }

  private void expandVariableKeys(Set<String> keys)
  {
    Map<String, Set<String>> variables = new LinkedHashMap<String, Set<String>>();
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

  public void recordVariables(Installation installation, Workspace workspace, User user)
  {
    recordRules(user.getAttributeRules(), true);

    AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(adapterFactory)
    {
      @Override
      public String getText(Object object)
      {
        String result = super.getText(object);
        if (object instanceof ProjectCatalog)
        {
          if (!result.endsWith("Projects"))
          {
            result += " Projects";
          }
        }
        else if (object instanceof ProductCatalog)
        {
          if (!result.endsWith("Products"))
          {
            result += " Products";
          }
        }

        return result;
      }
    };

    EList<SetupTask> userSetupTasks = user.getSetupTasks();
    if (!unresolvedVariables.isEmpty())
    {
      applyUnresolvedVariables(installation, workspace, user, unresolvedVariables, userSetupTasks, itemDelegator);
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
                if (workspaceScopeTasks == null)
                {
                  workspaceScopeTasks = workspace.getSetupTasks();
                }
                projectCatalogScopedVariables.add(unspecifiedVariable);
                break;
              }

              case PRODUCT_VERSION:
              case PRODUCT:
              case PRODUCT_CATALOG:
              case INSTALLATION:
              {
                if (installationScopeTasks == null)
                {
                  installationScopeTasks = installation.getSetupTasks();
                }
                productCatalogScopedVariables.add(unspecifiedVariable);

                break;
              }

              case USER:
              {
                if (workspace != null)
                {
                  if (workspaceScopeTasks == null)
                  {
                    workspaceScopeTasks = findOrCreate(itemDelegator, workspace, userSetupTasks).getSetupTasks();
                  }
                  projectCatalogScopedVariables.add(unspecifiedVariable);
                }

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

      applyUnresolvedVariables(installation, workspace, user, productCatalogScopedVariables, installationScopeTasks, itemDelegator);
      applyUnresolvedVariables(installation, workspace, user, projectCatalogScopedVariables, workspaceScopeTasks, itemDelegator);
    }
  }

  private URI getEffectiveStorage(VariableTask variable)
  {
    URI storageURI = variable.getStorageURI();
    if (variable.getType() == VariableType.PASSWORD)
    {
      if (VariableTask.DEFAULT_STORAGE_URI.equals(storageURI))
      {
        storageURI = PreferencesUtil.ROOT_PREFERENCE_NODE_URI.appendSegments(new String[] { PreferencesUtil.SECURE_NODE, SetupContext.OOMPH_NODE,
            variable.getName(), "" });
      }
      else if (storageURI != null && PreferencesUtil.PREFERENCE_SCHEME.equals(storageURI.scheme()) && !storageURI.hasTrailingPathSeparator())
      {
        storageURI = storageURI.appendSegment("");
      }
    }

    return storageURI;
  }

  public void savePasswords()
  {
    for (Map.Entry<URI, String> entry : passwords.entrySet())
    {
      String value = PreferencesUtil.decrypt(entry.getValue());
      if (!StringUtil.isEmpty(value) && !" ".equals(value))
      {
        URI storageURI = entry.getKey();
        URIConverter uriConverter = getURIConverter();
        try
        {
          Writer writer = ((URIConverter.WriteableOutputStream)uriConverter.createOutputStream(storageURI)).asWriter();
          writer.write(value);
          writer.close();
        }
        catch (IOException ex)
        {
          SetupCorePlugin.INSTANCE.log(ex);
        }
      }
    }
  }

  private void applyUnresolvedVariables(Installation installation, Workspace workspace, User user, Collection<VariableTask> variables,
      EList<SetupTask> rootTasks, AdapterFactoryItemDelegator itemDelegator)
  {
    Resource installationResource = installation.eResource();
    Resource workspaceResource = workspace == null ? null : workspace.eResource();
    Resource userResource = user.eResource();
    List<VariableTask> unspecifiedVariables = new ArrayList<VariableTask>();
    for (VariableTask variable : variables)
    {
      URI storageURI = getEffectiveStorage(variable);
      if (storageURI != null)
      {
        String value = variable.getValue();
        if (value != null)
        {
          if (variable.getType() == VariableType.PASSWORD)
          {
            passwords.put(storageURI, value);
          }
          else
          {
            Scope scope = variable.getScope();
            if (scope != null && storageURI.equals(VariableTask.DEFAULT_STORAGE_URI))
            {
              switch (scope.getType())
              {
                case INSTALLATION:
                {
                  apply(installationResource, scope, variable, value);
                  break;
                }
                case WORKSPACE:
                {
                  apply(workspaceResource, scope, variable, value);
                  break;
                }
                case USER:
                {
                  apply(userResource, scope, variable, value);
                  break;
                }
                default:
                {
                  unspecifiedVariables.add(variable);
                  break;
                }
              }
            }
            else
            {
              unspecifiedVariables.add(variable);
            }
          }
        }
      }
    }

    LOOP: for (VariableTask unspecifiedVariable : unspecifiedVariables)
    {
      String name = unspecifiedVariable.getName();
      String value = unspecifiedVariable.getValue();
      URI storageURI = unspecifiedVariable.getStorageURI();

      EList<SetupTask> targetSetupTasks = rootTasks;
      Scope scope = unspecifiedVariable.getScope();
      if (scope != null)
      {
        if (storageURI.equals(VariableTask.WORKSPACE_STORAGE_URI))
        {
          if (workspace != null)
          {
            targetSetupTasks = workspace.getSetupTasks();
          }
        }
        else if (storageURI.equals(VariableTask.INSTALLATION_STORAGE_URI))
        {
          targetSetupTasks = installation.getSetupTasks();
        }

        if (unspecifiedVariable.getAnnotation(AnnotationConstants.ANNOTATION_GLOBAL_VARIABLE) == null)
        {
          targetSetupTasks = findOrCreate(itemDelegator, scope, targetSetupTasks).getSetupTasks();
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

      VariableTask userPreference = EcoreUtil.copy(unspecifiedVariable);
      userPreference.getAnnotations().clear();
      userPreference.getChoices().clear();
      userPreference.setStorageURI(VariableTask.DEFAULT_STORAGE_URI);
      targetSetupTasks.add(userPreference);
    }
  }

  private void apply(Resource resource, Scope scope, VariableTask variable, String value)
  {
    String uriFragment = scope.eResource().getURIFragment(variable);
    EObject eObject = resource.getEObject(uriFragment);
    if (eObject instanceof VariableTask)
    {
      VariableTask targetVariable = (VariableTask)eObject;
      if (variable.getName().equals(targetVariable.getName()))
      {
        targetVariable.setValue(value);
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
      if (attribute.isChangeable() && attribute.getEAnnotation(EAnnotationConstants.ANNOTATION_NO_EXPAND) == null)
      {
        String instanceClassName = attribute.getEAttributeType().getInstanceClassName();
        ValueConverter valueConverter = CONVERTERS.get(instanceClassName);
        if (valueConverter != null)
        {
          if (attribute.isMany())
          {
            List<?> values = (List<?>)eObject.eGet(attribute);
            List<Object> newValues = new ArrayList<Object>();
            boolean failed = false;
            for (Object value : values)
            {
              String newValue = expandString(valueConverter.convertToString(value), keys);
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
                newValues.add(valueConverter.createFromString(newValue));
              }
            }

            if (!failed)
            {
              eObject.eSet(attribute, newValues);
            }
          }
          else
          {
            Object value = eObject.eGet(attribute);
            if (value != null)
            {
              String newValue;
              if (attribute == SetupPackage.Literals.VARIABLE_TASK__DEFAULT_VALUE)
              {
                // With second parameter not null, if value is not resolved, expandString returns newValue as null
                newValue = expandString(valueConverter.convertToString(value), new LinkedHashSet<String>());
                eObject.eSet(attribute, valueConverter.createFromString(newValue));
              }
              else if (attribute != SetupPackage.Literals.VARIABLE_TASK__VALUE)
              {
                newValue = expandString(valueConverter.convertToString(value), keys);
                if (newValue == null)
                {
                  unresolvedSettings.add(((InternalEObject)eObject).eSetting(attribute));
                }
                else
                {
                  Object object = valueConverter.createFromString(newValue);
                  if (!value.equals(object))
                  {
                    eObject.eSet(attribute, object);
                  }
                }
              }
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

      performEclipseIniTask(false, "--launcher.appendVmargs", null);
      performEclipseIniTask(true, "-D" + SetupProperties.PROP_UPDATE_URL, "=" + redirect(URI.createURI((String)get(SetupProperties.PROP_UPDATE_URL))));

      performIndexRediction(SetupContext.INDEX_SETUP_URI, "");
      performIndexRediction(SetupContext.INDEX_SETUP_LOCATION_URI, ".location");

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
        URI targetURI = URI.createFileURI(getProductConfigurationLocation().toString()).appendSegments(networkPreferences);

        ResourceCopyTask resourceCopyTask = SetupFactory.eINSTANCE.createResourceCopyTask();
        resourceCopyTask.setSourceURL(sourceLocation.toString());
        resourceCopyTask.setTargetURL(targetURI.toString());
        performTask(resourceCopyTask);
      }
    }

    hasSuccessfullyPerformed = true;
  }

  private void performIndexRediction(URI indexURI, String name) throws Exception
  {
    {
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
            performEclipseIniTask(true, "-D" + SetupProperties.PROP_REDIRECTION_BASE + "index" + name + ".redirection", "=" + baseBaseURI + "->"
                + redirectedBaseBaseURI);
          }
          else
          {
            performEclipseIniTask(true, "-D" + SetupProperties.PROP_REDIRECTION_BASE + "index" + name + ".redirection", "=" + baseURI + "->"
                + redirectedBaseURI);
          }
        }
        else
        {
          performEclipseIniTask(true, "-D" + SetupProperties.PROP_REDIRECTION_BASE + "index" + name + ".redirection", "=" + indexURI + "->" + redirectedURI);
        }
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

  private void performNeededSetupTasks() throws Exception
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

      logBundleInfos();

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
      if (Boolean.TRUE.equals(autoBuilding))
      {
        // Disable the PDE's API analysis builder, if it's installed, and remember its previously current state.
        // It's considered disabled if it's not installed at all.
        final boolean disabled = PDEAPIUtil.setDisableAPIAnalysisBuilder(true);

        Job buildJob = new Job("Build")
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            try
            {
              EcorePlugin.getWorkspaceRoot().getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
              return Status.OK_STATUS;
            }
            catch (CoreException ex)
            {
              return SetupCorePlugin.INSTANCE.getStatus(ex);
            }
            finally
            {
              try
              {
                restoreAutoBuilding(true);
              }
              catch (CoreException ex)
              {
                SetupCorePlugin.INSTANCE.log(ex);
              }

              if (!disabled)
              {
                // Restore it to false if it was true before we set it to false;
                PDEAPIUtil.setDisableAPIAnalysisBuilder(false);
              }
            }
          }

          @Override
          public boolean belongsTo(Object family)
          {
            return ResourcesPlugin.FAMILY_MANUAL_BUILD == family;
          }
        };

        buildJob.setRule(EcorePlugin.getWorkspaceRoot());
        buildJob.schedule();
      }
    }
  }

  private void logBundleInfos()
  {
    List<String> bundleInfos = new ArrayList<String>();
    for (Bundle bundle : bundles)
    {
      StringBuilder builder = new StringBuilder("Bundle ");
      builder.append(bundle.getSymbolicName());
      builder.append(" ");
      builder.append(bundle.getVersion());

      InputStream source = null;

      try
      {
        URL url = bundle.getResource("about.mappings");
        if (url != null)
        {
          source = url.openStream();

          Properties properties = new Properties();
          properties.load(source);

          String buildID = (String)properties.get("0");
          if (buildID != null && !buildID.startsWith("$"))
          {
            builder.append(", build=");
            builder.append(buildID);
          }

          String gitBranch = (String)properties.get("1");
          if (gitBranch != null && !gitBranch.startsWith("$"))
          {
            builder.append(", branch=");
            builder.append(gitBranch);
          }

          String gitCommit = (String)properties.get("2");
          if (gitCommit != null && !gitCommit.startsWith("$"))
          {
            builder.append(", commit=");
            builder.append(gitCommit);
          }
        }
      }
      catch (IOException ex)
      {
        //$FALL-THROUGH$
      }
      finally
      {
        IOUtil.closeSilent(source);
      }

      bundleInfos.add(builder.toString());
    }

    Collections.sort(bundleInfos);
    for (String bundleInfo : bundleInfos)
    {
      log(bundleInfo);
    }
  }

  private Map<SetupTask, SetupTask> getSubstitutions(EList<SetupTask> setupTasks)
  {
    Map<Object, SetupTask> overrides = new LinkedHashMap<Object, SetupTask>();
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
    Map<Resource, Resource> resourceCopies = new LinkedHashMap<Resource, Resource>();

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

    Map<URI, EObject> originalCrossReferences = new LinkedHashMap<URI, EObject>();
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

    Map<EObject, EObject> originalCopier = new LinkedHashMap<EObject, EObject>(copier);
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

    SetupUtil.reorder(list, new SetupUtil.DependencyProvider<Map.Entry<String, Set<String>>>()
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

    final Map<SetupTask, Set<SetupTask>> dependencies = new LinkedHashMap<SetupTask, Set<SetupTask>>();
    for (SetupTask setupTask : setupTasks)
    {
      CollectionUtil.addAll(dependencies, setupTask, setupTask.getPredecessors());

      for (SetupTask successor : setupTask.getSuccessors())
      {
        CollectionUtil.add(dependencies, successor, setupTask);
      }
    }

    SetupUtil.reorder(setupTasks, new SetupUtil.DependencyProvider<SetupTask>()
    {
      public Collection<SetupTask> getDependencies(SetupTask setupTask)
      {
        return dependencies.get(setupTask);
      }
    });

    // Set up the predecessor dependencies so these tasks will not be reordered relative to each other when they are merged with tasks from other streams.
    SetupTask previousSetupTask = null;
    for (SetupTask setupTask : setupTasks)
    {
      setupTask.getSuccessors().clear();
      EList<SetupTask> predecessors = setupTask.getPredecessors();
      predecessors.clear();

      if (!(setupTask instanceof VariableTask))
      {
        if (previousSetupTask != null)
        {
          predecessors.add(previousSetupTask);
        }

        previousSetupTask = setupTask;
      }
    }
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

    Map<Object, Set<Object>> composedMap = new HashMap<Object, Set<Object>>();
    List<VariableTask> allAppliedRuleVariables = new ArrayList<VariableTask>();
    List<VariableTask> allUnresolvedVariables = new ArrayList<VariableTask>();
    List<VariableTask> allPasswordVariables = new ArrayList<VariableTask>();
    Map<VariableTask, EAttribute> allRuleAttributes = new LinkedHashMap<VariableTask, EAttribute>();

    Workspace workspace = setupContext.getWorkspace();
    List<Stream> streams = workspace == null ? null : workspace.getStreams();
    if (streams == null || streams.isEmpty())
    {
      streams = Collections.singletonList(null);
    }

    for (Stream stream : streams)
    {
      if (stream == null || !stream.eIsProxy())
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
            variable.setStorageURI(null);
            unresolvedVariables.add(variable);
            demandCreatedUnresolvedVariables.add(variable);
          }

          undeclaredVariables.clear();
        }

        CollectionUtil.putAll(composedMap, performer.getMap());

        if (fullPrompt)
        {
          SetupContext fullPromptContext = SetupContext.createCopy(setupContext.getInstallation(), setupContext.getWorkspace(), setupContext.getUser());

          Set<VariableTask> variables = new LinkedHashSet<VariableTask>();
          final SetupTaskPerformer partialPromptPerformer = performer;

          prepareFullPrompt(variables, fullPromptContext.getInstallation());
          prepareFullPrompt(variables, fullPromptContext.getWorkspace());

          User user = fullPromptContext.getUser();
          prepareFullPrompt(variables, user);
          user.getAttributeRules().clear();

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
                    AttributeRule attributeRule = partialPromptPerformer.getAttributeRule(eAttribute, false);
                    if (attributeRule != null)
                    {
                      String value = prompter.getValue(variable);
                      variable.setValue(value == null ? attributeRule.getValue() : value);
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
          CollectionUtil.putAll(composedMap, performer.getMap());
          performer = fullPromptPerformer;
        }

        allAppliedRuleVariables.addAll(performer.getAppliedRuleVariables());
        allUnresolvedVariables.addAll(performer.getUnresolvedVariables());
        allPasswordVariables.addAll(performer.getPasswordVariables());
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
    Set<Bundle> bundles = new HashSet<Bundle>();
    for (SetupTaskPerformer performer : performers)
    {
      setupTasks.addAll(performer.getTriggeredSetupTasks());
      bundles.addAll(performer.getBundles());
    }

    SetupTaskPerformer composedPerformer = new SetupTaskPerformer(uriConverter, prompter, trigger, setupContext, setupTasks);
    composedPerformer.getBundles().addAll(bundles);
    composedPerformer.getAppliedRuleVariables().addAll(allAppliedRuleVariables);
    composedPerformer.getUnresolvedVariables().addAll(allUnresolvedVariables);
    composedPerformer.getPasswordVariables().addAll(allPasswordVariables);
    composedPerformer.getRuleAttributes().putAll(allRuleAttributes);
    composedPerformer.redirectTriggeredSetupTasks();

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

    File configurationLocation = composedPerformer.getProductConfigurationLocation();
    if (configurationLocation != null)
    {
      File installationLocation = new File(configurationLocation, "org.eclipse.oomph.setup/installation.setup");
      URI installationURI = URI.createFileURI(installationLocation.toString());
      for (SetupTaskPerformer performer : performers)
      {
        performer.getInstallation().eResource().setURI(installationURI);
      }
    }

    Map<Object, Object> finalComposedMap = composedPerformer.getMap();
    for (Map.Entry<Object, Set<Object>> entry : composedMap.entrySet())
    {
      Object key = entry.getKey();
      if (!finalComposedMap.containsKey(key))
      {
        Set<Object> value = entry.getValue();
        value.remove(null);
        value.remove("");
        if (value.size() == 1)
        {
          finalComposedMap.put(key, value.iterator().next());
        }
      }
    }

    return composedPerformer;
  }

  public static Set<? extends Authenticator> getAuthenticators(VariableTask variable)
  {
    VariableAdapter variableAdapter = (VariableAdapter)EcoreUtil.getExistingAdapter(variable, VariableAdapter.class);
    if (variableAdapter != null)
    {
      return variableAdapter.getAuthenticators(variable);
    }

    return null;
  }

  private static class VariableAdapter extends AdapterImpl
  {
    private SetupTaskPerformer performer;

    public VariableAdapter(SetupTaskPerformer perform)
    {
      performer = perform;
    }

    @Override
    public boolean isAdapterForType(Object type)
    {
      return type == VariableAdapter.class;
    }

    protected Set<? extends Authenticator> getAuthenticators(final VariableTask variable)
    {
      StringExpander stringExpander = new StringExpander()
      {
        @Override
        protected String resolve(String key)
        {
          String value = getValue(key);
          return StringUtil.isEmpty(value) ? performer.resolve(key) : value;
        }

        @Override
        protected boolean isUnexpanded(String key)
        {
          return performer.isUnexpanded(key) && StringUtil.isEmpty(getValue(key));
        }

        private String getValue(String key)
        {
          VariableTask variable = performer.allVariables.get(key);
          if (variable != null)
          {
            return performer.getPrompter().getValue(variable);
          }

          return null;
        }

        @Override
        protected String filter(String value, String filterName)
        {
          return performer.filter(value, filterName);
        }
      };

      return Authenticator.create(variable, stringExpander);
    }
  }

  private static class FullPromptMarker extends AdapterImpl
  {
    @Override
    public boolean isAdapterForType(Object type)
    {
      return type == FullPromptMarker.class;
    }
  }

  private static boolean isFullPromptUser(User user)
  {
    return EcoreUtil.getExistingAdapter(user, FullPromptMarker.class) != null;
  }

  private static void prepareFullPrompt(Set<VariableTask> variables, ModelElement modelElement)
  {
    if (modelElement != null)
    {
      for (Iterator<EObject> it = modelElement.eAllContents(); it.hasNext();)
      {
        EObject eObject = it.next();
        if (eObject instanceof VariableTask)
        {
          VariableTask variableTask = (VariableTask)eObject;
          variables.add(variableTask);
          variableTask.setValue(null);
        }
      }

      setFullPromptMarker(modelElement);
    }
  }

  private static void setFullPromptMarker(ModelElement modelElement)
  {
    modelElement.eAdapters().add(new FullPromptMarker());
  }

  public void setProgress(ProgressLog progress)
  {
    this.progress = progress;
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

  public static EList<SetupTask> createEnablementTasks(EModelElement eModelElement, boolean withVariables)
  {
    EList<SetupTask> enablementTasks = null;

    for (EAnnotation eAnnotation : eModelElement.getEAnnotations())
    {
      String source = eAnnotation.getSource();
      if (EAnnotationConstants.ANNOTATION_ENABLEMENT.equals(source))
      {
        if (enablementTasks == null)
        {
          enablementTasks = new BasicEList<SetupTask>();
        }

        String repositoryLocation = eAnnotation.getDetails().get(EAnnotationConstants.KEY_REPOSITORY);
        if (!StringUtil.isEmpty(repositoryLocation))
        {
          if (withVariables)
          {
            String variableName = eAnnotation.getDetails().get(EAnnotationConstants.KEY_VARIABLE_NAME);
            if (!StringUtil.isEmpty(variableName))
            {
              VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
              variable.setName(variableName);
              variable.setValue(repositoryLocation);
              enablementTasks.add(0, variable);

              repositoryLocation = getVariableReference(variableName);
            }
          }
        }

        P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
        EList<Requirement> requirements = p2Task.getRequirements();
        String ius = eAnnotation.getDetails().get(EAnnotationConstants.KEY_INSTALLABLE_UNITS);
        if (!StringUtil.isEmpty(ius))
        {
          for (String requirementSpecification : ius.split("\\s"))
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
        }

        if (!StringUtil.isEmpty(repositoryLocation))
        {
          Repository repository = P2Factory.eINSTANCE.createRepository(repositoryLocation);
          p2Task.getRepositories().add(repository);
        }

        // Ensure that these are first so that these are the targets for merging rather than the sources.
        // The latter causes problems in the copier.
        enablementTasks.add(0, p2Task);
      }
    }

    EObject eContainer = eModelElement.eContainer();
    if (eContainer instanceof EModelElement)
    {
      EList<SetupTask> containerEnablementTasks = createEnablementTasks((EModelElement)eContainer, withVariables);
      if (containerEnablementTasks != null)
      {
        if (enablementTasks == null)
        {
          enablementTasks = containerEnablementTasks;
        }
        else
        {
          enablementTasks.addAll(containerEnablementTasks);
        }
      }
    }

    return enablementTasks;
  }

  private static String getVariableReference(String variableName)
  {
    return "${" + variableName + "}";
  }

  protected static class ValueConverter
  {
    public Object createFromString(String literal)
    {
      return literal;
    }

    public String convertToString(Object value)
    {
      return value == null ? null : value.toString();
    }
  }

  protected static class URIValueConverter extends ValueConverter
  {
    @Override
    public Object createFromString(String literal)
    {
      return BaseFactory.eINSTANCE.createURI(literal);
    }
  }

  private static class PDEAPIUtil
  {
    private static final Field BUILD_DISABLED_FIELD;
    static
    {
      Field buildDisabledField = null;
      // Disable API analysis building for the initial build.
      try
      {
        Class<?> apiAnalysisBuilder = CommonPlugin.loadClass("org.eclipse.pde.api.tools", "org.eclipse.pde.api.tools.internal.builder.ApiAnalysisBuilder");
        buildDisabledField = apiAnalysisBuilder.getDeclaredField("buildDisabled");
        buildDisabledField.setAccessible(true);
      }
      catch (Exception ex)
      {
        // Ignore
      }

      BUILD_DISABLED_FIELD = buildDisabledField;
    }

    private static boolean setDisableAPIAnalysisBuilder(boolean disabled)
    {
      if (BUILD_DISABLED_FIELD != null)
      {
        try
        {
          boolean result = (Boolean)BUILD_DISABLED_FIELD.get(null);
          if (result != disabled)
          {
            BUILD_DISABLED_FIELD.set(null, disabled);
          }

          return result;
        }
        catch (Exception ex)
        {
          // Ignore.
        }
      }

      return true;
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
