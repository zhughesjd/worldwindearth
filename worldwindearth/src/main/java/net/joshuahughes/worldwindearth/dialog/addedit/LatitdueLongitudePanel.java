package net.joshuahughes.worldwindearth.dialog.addedit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class LatitdueLongitudePanel extends AbstractPanel
{
    private static final long serialVersionUID = -1260396815326982917L;
    JTextField latField = new JTextField(){private static final long serialVersionUID = 1L;{setName("Latitude");}};
    JTextField lonField = new JTextField(){private static final long serialVersionUID = 1L;{setName("Longitude");}};
    public LatitdueLongitudePanel(){
        super(new GridBagLayout( ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=gbc.gridy=0;
        gbc.weighty=1;
        for(JTextField field : new JTextField[]{latField,lonField}){
            gbc.weightx=.1;
            gbc.gridx=0;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.LINE_END;
            add(new JLabel(field.getName( )+":"),gbc);
            gbc.weightx=.9;
            gbc.anchor = GridBagConstraints.LINE_START;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx++;
            add(field,gbc);
            gbc.gridy++;
        }
    }
}
