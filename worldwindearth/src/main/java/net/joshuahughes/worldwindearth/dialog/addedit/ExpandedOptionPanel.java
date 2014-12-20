package net.joshuahughes.worldwindearth.dialog.addedit;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class ExpandedOptionPanel extends AbstractPanel
{
    private static final long serialVersionUID = -1260396815326982917L;
    JCheckBox expandedBox = new JCheckBox("Allow this folder to be expanded",true);
    JCheckBox optionsBox = new JCheckBox("Show content as options (radio button selection)");
    public ExpandedOptionPanel(){
        super(new GridLayout(2,1));
        add(expandedBox);
        expandedBox.addActionListener( new ActionListener( )
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                optionsBox.setEnabled( expandedBox.isSelected( ) );
            }
        } );
        add(optionsBox);
    }
}
