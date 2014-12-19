package net.joshuahughes.worldwindearth.dialog.addedit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class ImagePhotoPanel extends UpperPanel
{
    private static final long serialVersionUID = -1260396815326982917L;
    JTextField linkField = new JTextField();
    JSlider transparencySlider = new JSlider(0,255,1);
    public ImagePhotoPanel(){
        super(new GridBagLayout( ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=gbc.gridy=0;
        gbc.weightx=gbc.weighty=0;
        add(new JLabel("Link:"),gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(linkField,gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.NONE;
        add(getBrowseButton(),gbc);
        
        gbc.gridx=0;
        gbc.gridy++;
        add(new JLabel("Transparency:"));
    
        gbc.gridx=0;
        gbc.gridy++;
        add(new JLabel("Clear"),gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(transparencySlider,gbc);
        gbc.gridx++;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Opaque"),gbc);

    }
    private JButton getBrowseButton( )
    {
        JButton button = new JButton("Browse...");
        return button;
    }
}
