package net.joshuahughes.worldwindearth.dialog.addedit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class LinkPanel extends AbstractPanel
{
    private static final long serialVersionUID = -1260396815326982917L;
    JTextField linkField = new JTextField();
    JSlider transparencySlider = new JSlider(0,255,1);
    public LinkPanel(){
        super(new GridBagLayout( ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=gbc.gridy=0;
        gbc.weightx=0;
        gbc.weighty=1;
        gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Link:"),gbc);
        gbc.gridx++;
        gbc.weightx=1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(linkField,gbc);
        gbc.gridx++;
        gbc.weightx=0;
        gbc.fill = GridBagConstraints.NONE;
        JButton button = new JButton("Browse...");
        add(button,gbc);
    }
}
