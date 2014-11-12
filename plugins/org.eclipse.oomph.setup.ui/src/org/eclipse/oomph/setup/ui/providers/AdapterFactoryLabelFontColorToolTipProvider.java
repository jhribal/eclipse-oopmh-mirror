/**
 * <copyright>
 * Copyright (c) 2010-2012 Henshin developers. All rights reserved.
 * This program and the accompanying materials are made available
 * under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * </copyright>
 */
package org.eclipse.oomph.setup.ui.providers;

import org.eclipse.oomph.base.provider.IItemToolTipProvider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.viewers.Viewer;

/**
 * AdapterFactory-based LabelProvider which implements {@link IToolTipProvider}
 * by delegating to an adapter implementing {@link IItemToolTipProvider}.
 *
 * @author Gregor Bonifer
 *
 */
public class AdapterFactoryLabelFontColorToolTipProvider extends AdapterFactoryLabelProvider.FontAndColorProvider implements IToolTipProvider
{

  public AdapterFactoryLabelFontColorToolTipProvider(AdapterFactory arg0, Viewer arg1)
  {
    super(arg0, arg1);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.emf.henshin.editor.util.IToolTipProvider#getToolTip(java.lang.Object)
   */
  public Object getToolTip(Object object)
  {
    IItemToolTipProvider tipProvider = getItemToolTipProvider(object);
    return tipProvider == null ? null : tipProvider.getToolTip(object);
  }

  protected IItemToolTipProvider getItemToolTipProvider(Object object)
  {
    Object adapter = adapterFactory.adapt(object, IItemToolTipProvider.class);
    if (adapter instanceof IItemToolTipProvider)
    {
      return (IItemToolTipProvider)adapter;
    }
    return null;
  }

}
