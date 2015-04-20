package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public class ImageHoverButton extends SimpleInstallerButton
{
  private Image defaultImage;

  private Image hoverImage;

  private Image disabledImage;

  public ImageHoverButton(Composite parent, int buttonStyle, Image image, Image hoverImage)
  {
    super(parent, buttonStyle);
    defaultImage = image;
    this.hoverImage = hoverImage;

    setImage(computeImage());
  }

  protected Image computeImage()
  {
    if (!isEnabled())
    {
      return getDisabledImage() != null ? getDisabledImage() : getDefaultImage();
    }
    return isHover() ? getHoverImage() : getDefaultImage();
  }

  @Override
  public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY)
  {
    // No background
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    setImage(computeImage());
  }

  protected Image getDefaultImage()
  {
    return defaultImage;
  }

  protected Image getHoverImage()
  {
    return hoverImage;
  }

  @Override
  protected void onHover()
  {
    setImage(computeImage());
  }

  public Image getDisabledImage()
  {
    return disabledImage;
  }

  public void setDisabledImage(Image disabledImage)
  {
    if (this.disabledImage != disabledImage)
    {
      this.disabledImage = disabledImage;
      redraw();
    }
  }

}
