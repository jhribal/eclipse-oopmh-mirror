package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class ImageCheckbox extends ImageHoverButton
{
  private Image checkedImage;

  private boolean checked;

  public ImageCheckbox(Composite parent, Image defaultImage, Image hoverImage)
  {
    this(parent, defaultImage, hoverImage, hoverImage);
  }

  public ImageCheckbox(Composite parent, Image defaultImage, Image hoverImage, Image checkedImage)
  {
    super(parent, SWT.CHECK, defaultImage, hoverImage);
    this.checkedImage = checkedImage;
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
    setImage(computeImage());
    redraw();
  }

  @Override
  protected Image computeImage()
  {
    if (!isEnabled())
    {
      return super.computeImage();
    }
    if (isHover() && !isChecked())
    {
      return getHoverImage();
    }
    return isChecked() ? getCheckedImage() : getDefaultImage();
  }

  protected Image getCheckedImage()
  {
    return checkedImage;
  }

}
