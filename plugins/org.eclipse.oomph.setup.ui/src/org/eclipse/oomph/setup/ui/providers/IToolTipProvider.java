package org.eclipse.oomph.setup.ui.providers;

/**
 * Implementation interface for ToolTipProviders as required by
 * {@link TreeViewerToolTipSupport}.
 *
 * @author Gregor Bonifer
 *
 */
public interface IToolTipProvider
{

  public Object getToolTip(Object object);

}
