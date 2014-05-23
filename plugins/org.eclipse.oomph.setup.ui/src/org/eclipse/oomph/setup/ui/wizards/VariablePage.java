/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Ericsson AB (Julian Enoch) - Bug 434525 - Allow prompted variables to be pre-populated
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.p2.LicenseConfirmation;
import org.eclipse.oomph.p2.LicensePrompter;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.LicenseDialog;
import org.eclipse.oomph.setup.ui.PropertyField;
import org.eclipse.oomph.setup.ui.PropertyField.ValueListener;
import org.eclipse.oomph.setup.ui.bundle.SetupUIPlugin;
import org.eclipse.oomph.ui.UICallback;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class VariablePage extends SetupWizardPage implements SetupPrompter, LicensePrompter
{
  private Composite composite;

  private ScrolledComposite scrolledComposite;

  private Map<URI, FieldHolder> fieldHolders = new HashMap<URI, FieldHolder>();

  private boolean focusSet;

  private boolean prompted;

  private boolean fullPrompt;

  private List<SetupTaskPerformer> promptedPerformers = new ArrayList<SetupTaskPerformer>();

  private SetupTaskPerformer performer;

  protected VariablePage()
  {
    super("VariablePage");
    setTitle("Variables");
    setDescription("Enter values for the required variables.");

  }

  @Override
  protected Control createUI(Composite parent)
  {
    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(new GridLayout(1, false));
    mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

    scrolledComposite = new ScrolledComposite(mainComposite, SWT.VERTICAL);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

    GridLayout layout = new GridLayout(3, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.horizontalSpacing = 10;
    layout.verticalSpacing = 10;

    composite = new Composite(scrolledComposite, SWT.NONE);
    composite.setLayout(layout);
    scrolledComposite.setContent(composite);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));

    ControlAdapter resizeListener = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent event)
      {
        Point size = composite.computeSize(scrolledComposite.getClientArea().width, SWT.DEFAULT);
        scrolledComposite.setMinSize(size);
      }
    };

    scrolledComposite.addControlListener(resizeListener);
    composite.addControlListener(resizeListener);
    composite.notifyListeners(SWT.Resize, new Event());

    final Button fullPromptButton = new Button(mainComposite, SWT.CHECK);
    fullPromptButton.setText("Show all variables");
    fullPromptButton.setLayoutData(new GridData());
    fullPromptButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        fullPrompt = fullPromptButton.getSelection();
        validate();
      }
    });

    return mainComposite;
  }

  private URI getURI(VariableTask variable)
  {
    String name = variable.getName();
    URI uri = variable.eResource() == null ? URI.createURI("#") : EcoreUtil.getURI(variable);
    uri = uri.appendFragment(uri.fragment() + "~" + name);
    return uri;
  }

  private FieldHolder getFieldHolder(VariableTask variable)
  {
    return fieldHolders.get(getURI(variable));
  }

  private FieldHolder createFieldHolder(VariableTask variable)
  {
    URI uri = getURI(variable);
    FieldHolder fieldHolder = fieldHolders.get(uri);
    if (fieldHolder == null)
    {
      PropertyField<?> field = createField(variable);
      field.fill(composite);

      fieldHolder = new FieldHolder(field, variable);
      fieldHolders.put(uri, fieldHolder);
    }
    else
    {
      fieldHolder.add(variable);
    }

    return fieldHolder;
  }

  private PropertyField<?> createField(final VariableTask variable)
  {
    PropertyField<?> field;

    EList<VariableChoice> choices = variable.getChoices();
    if (!choices.isEmpty())
    {
      field = new PropertyField.ChoiceField<Control>(choices);
    }
    else
    {
      field = createField(variable.getType());
    }

    String label = variable.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = variable.getName();
    }

    for (Adapter adapter : variable.eAdapters())
    {
      if (adapter == SetupTaskPerformer.RULE_VARIABLE_ADAPTER)
      {
        label += " (rule)";
        break;
      }
    }

    field.setValue(variable.getDefault());
    field.setLabelText(label);
    field.setToolTip(variable.getDescription());

    GridData gridData = field.getLabelGridData();
    gridData.widthHint = 150;

    return field;
  }

  private PropertyField<?> createField(VariableType type)
  {
    switch (type)
    {
      case FOLDER:
        PropertyField.FileField fileField = new PropertyField.FileField();
        fileField.setDialogText("Folder Selection");
        fileField.setDialogMessage("Select a folder.");
        return fileField;

      case PASSWORD:
        return new PropertyField.TextField<Control>(true);
    }

    return new PropertyField.TextField<Control>();
  }

  private void updateFields()
  {
    // Clear out the variables from any of the fields previous created.
    for (FieldHolder fieldHolder : fieldHolders.values())
    {
      fieldHolder.clear();
    }

    for (SetupTaskPerformer setupTaskPerformer : promptedPerformers)
    {
      List<VariableTask> variables = setupTaskPerformer.getUnresolvedVariables();
      for (VariableTask variable : variables)
      {
        FieldHolder fieldHolder;

        VariableTask ruleVariable = setupTaskPerformer.getRuleVariable(variable);
        if (ruleVariable == null)
        {
          fieldHolder = createFieldHolder(variable);
        }
        else
        {
          fieldHolder = createFieldHolder(ruleVariable);
          fieldHolder.add(variable);
        }

        if (!focusSet)
        {
          fieldHolder.getField().setFocus();
          focusSet = true;
        }
      }
    }

    for (Iterator<Map.Entry<URI, FieldHolder>> it = fieldHolders.entrySet().iterator(); it.hasNext();)
    {
      Entry<URI, FieldHolder> entry = it.next();
      FieldHolder fieldHolder = entry.getValue();
      for (VariableTask variable : fieldHolder.getVariables())
      {
        String value = variable.getValue();
        if (!StringUtil.isEmpty(value))
        {
          fieldHolder.getField().setValue(value);
        }
      }
    }

    // Determine the URIs of all the variables actually being used.
    Set<URI> uris = new HashSet<URI>();
    if (performer != null)
    {
      for (VariableTask variable : performer.getUnresolvedVariables())
      {
        uris.add(getURI(variable));
      }
    }

    // Garbage collect any unused fields.
    for (Iterator<Map.Entry<URI, FieldHolder>> it = fieldHolders.entrySet().iterator(); it.hasNext();)
    {
      Entry<URI, FieldHolder> entry = it.next();
      FieldHolder fieldHolder = entry.getValue();
      if (!uris.contains(entry.getKey()) && fieldHolder.getVariables().isEmpty())
      {
        fieldHolder.dispose();
        it.remove();
      }
    }

    Composite parent = composite.getParent();
    parent.setRedraw(false);
    parent.pack();
    parent.getParent().layout();
    parent.setRedraw(true);
  }

  private void validate()
  {
    try
    {
      performer = null;

      try
      {
        promptedPerformers.clear();

        User originalUser = getUser();
        URI uri = originalUser.eResource().getURI();
        User user = EcoreUtil.copy(originalUser);
        Resource userResource = Resource.Factory.Registry.INSTANCE.getFactory(uri).createResource(uri);
        userResource.getContents().add(user);

        Trigger trigger = getTrigger();
        Installation installation = getInstallation();
        Workspace workspace = getWorkspace();

        performer = SetupTaskPerformer
            .create(getResourceSet().getURIConverter(), this, trigger, SetupContext.create(installation, workspace, user), fullPrompt);
        if (performer != null)
        {
          performer.put(LicensePrompter.class, this);
        }
      }
      catch (OperationCanceledException ex)
      {
        //$FALL-THROUGH$
      }

      if (performer == null)
      {
        setPageComplete(false);

        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            updateFields();
          }
        });
      }
      else
      {
        setPageComplete(true);

        if (!prompted)
        {
          advanceToNextPage();
        }
      }
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex);
    }
  }

  @Override
  public void enterPage(boolean forward)
  {
    performer = getWizard().getPerformer();
    if (performer != null && forward)
    {
      performer.setPrompter(this);
      setPageComplete(true);
      advanceToNextPage();
    }
    else
    {
      getWizard().setSetupContext(SetupContext.create(getInstallation(), getWorkspace(), SetupContext.createUserOnly(getResourceSet()).getUser()));
      setPageComplete(false);
      validate();
    }
  }

  @Override
  public void leavePage(boolean forward)
  {
    if (forward)
    {
      List<VariableTask> unresolvedVariables = performer.getUnresolvedVariables();
      for (FieldHolder fieldHolder : fieldHolders.values())
      {
        for (VariableTask variable : fieldHolder.getVariables())
        {
          unresolvedVariables.add(variable);
        }
      }

      User user = getUser();
      User copiedUser = EcoreUtil.copy(user);
      URI userResourceURI = user.eResource().getURI();
      Resource userResource = getResourceSet().getResourceFactoryRegistry().getFactory(userResourceURI).createResource(userResourceURI);
      userResource.getContents().add(copiedUser);

      int xxx; // This code is just like in org.eclipse.oomph.setup.ui.wizards.ProgressPage.saveLocalFiles(SetupTaskPerformer)
      Installation installation = getInstallation();
      Resource installationResource = installation.eResource();
      URI installationResourceURI = installationResource.getURI();
      installationResource.setURI(URI.createFileURI(new File(performer.getProductLocation(), "configuration/org.eclipse.oomph.setup/installation.setup")
          .toString()));

      Workspace workspace = getWorkspace();
      Resource workspaceResource = null;
      URI workspaceResourceURI = null;
      if (workspace != null)
      {
        workspaceResource = workspace.eResource();
        workspaceResourceURI = workspaceResource.getURI();
        workspaceResource.setURI(URI.createFileURI(new File(performer.getWorkspaceLocation(), ".metadata/.plugins/org.eclipse.oomph.setup/workspace.setup")
            .toString()));
      }

      performer.recordVariables(copiedUser);

      EList<AttributeRule> copiedAttributeRules = copiedUser.getAttributeRules();
      for (AttributeRule attributeRule : performer.getUser().getAttributeRules())
      {
        boolean set = false;
        for (AttributeRule copiedAttributeRule : copiedAttributeRules)
        {
          if (copiedAttributeRule.getAttributeURI().equals(attributeRule.getAttributeURI()))
          {
            copiedAttributeRule.setValue(attributeRule.getValue());
            set = true;
            break;
          }
        }

        if (!set)
        {
          copiedAttributeRules.add(EcoreUtil.copy(attributeRule));
        }
      }

      installationResource.setURI(installationResourceURI);
      if (workspaceResource != null)
      {
        workspaceResource.setURI(workspaceResourceURI);
      }

      unresolvedVariables.clear();

      getWizard().setSetupContext(SetupContext.create(getInstallation(), getWorkspace(), copiedUser));
      setPerformer(performer);
    }
    else
    {
      setPerformer(null);
    }
  }

  public LicenseConfirmation promptLicenses(final Map<ILicense, List<IInstallableUnit>> licensesToIUs)
  {
    final LicenseConfirmation[] result = { LicenseConfirmation.DECLINE };

    Display.getDefault().syncExec(new Runnable()
    {
      public void run()
      {
        LicenseDialog dialog = new LicenseDialog(null, licensesToIUs);
        if (dialog.open() == LicenseDialog.OK)
        {
          if (dialog.isRememberAcceptedLicenses())
          {
            result[0] = LicenseConfirmation.ACCEPT_AND_REMEMBER;
          }
          else
          {
            result[0] = LicenseConfirmation.ACCEPT;
          }
        }
      }
    });

    return result[0];
  }

  public String getValue(VariableTask variable)
  {
    FieldHolder fieldHolder = getFieldHolder(variable);
    if (fieldHolder != null)
    {
      String value = fieldHolder.getField().getValue();
      if (!"".equals(value))
      {
        return value;
      }
    }

    return null;
  }

  public boolean promptVariables(List<? extends SetupTaskContext> contexts)
  {
    prompted = true;

    @SuppressWarnings("unchecked")
    List<SetupTaskPerformer> performers = (List<SetupTaskPerformer>)contexts;
    for (SetupTaskPerformer performer : performers)
    {
      boolean resolvedAll = true;
      for (VariableTask variable : performer.getUnresolvedVariables())
      {
        FieldHolder fieldHolder = getFieldHolder(variable);
        if (fieldHolder != null)
        {
          String value = fieldHolder.getField().getValue();
          if (!"".equals(value))
          {
            variable.setValue(value);
          }
          else
          {
            resolvedAll = false;
          }
        }
        else
        {
          resolvedAll = false;
        }
      }

      if (!resolvedAll)
      {
        promptedPerformers.add(performer);
      }
    }

    return promptedPerformers.isEmpty();
  }

  public UserCallback getUserCallback()
  {
    return new UICallback(getShell(), AbstractSetupDialog.SHELL_TEXT);
  }

  /**
   * @author Ed Merks
   */
  private final class FieldHolder implements ValueListener
  {
    private Set<VariableTask> variables = new LinkedHashSet<VariableTask>();

    private PropertyField<?> field;

    public FieldHolder(PropertyField<?> field, VariableTask variable)
    {
      super();
      this.field = field;
      field.addValueListener(this);
      variables.add(variable);
    }

    public PropertyField<?> getField()
    {
      return field;
    }

    public Set<VariableTask> getVariables()
    {
      return variables;
    }

    public void clear()
    {
      variables.clear();
    }

    public void add(VariableTask variable)
    {
      if (variables.add(variable))
      {
        String value = field.getValue();
        if ("".equals(value))
        {
          variable.setValue(value);
        }
      }
    }

    public void valueChanged(String oldValue, String newValue) throws Exception
    {
      for (VariableTask variable : variables)
      {
        variable.setValue(newValue);
      }

      validate();
    }

    public void dispose()
    {
      field.dispose();
    }
  }
}
