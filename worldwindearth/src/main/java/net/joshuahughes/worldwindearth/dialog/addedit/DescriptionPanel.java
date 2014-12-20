package net.joshuahughes.worldwindearth.dialog.addedit;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DescriptionPanel extends AbstractPanel
{
    private static final long serialVersionUID = -1260396815326982917L;
    JButton linkButton = new JButton("Add Link...");
    JButton imageButton = new JButton("Add Image...");
    JButton okButton = new JButton("Ok");
    JButton cancelButton = new JButton("Cancel");
    JTextField urlField = new JTextField();
    JLabel urlLabel = new JLabel("error:");
    JPanel urlPanel = new JPanel(new BorderLayout());
    JTextArea descriptionArea = new JTextArea();
    public DescriptionPanel(){
        super(new GridBagLayout( ));
        setName("Description");
        JPanel okCancelPanel = new JPanel(new BorderLayout());
        okCancelPanel.add(okButton,BorderLayout.CENTER);
        okCancelPanel.add(cancelButton,BorderLayout.EAST);
        urlPanel.add(urlLabel,BorderLayout.WEST);
        urlPanel.add(urlField,BorderLayout.CENTER);
        urlPanel.add(okCancelPanel,BorderLayout.EAST);
        linkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				urlLabel.setText("Link URL:");
				doLayout(true);
			}
		});
        imageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				urlLabel.setText("Image URL:");
				doLayout(true);
			}
		});
        okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = urlField.getText();
				String textToInsert =
						urlLabel.getText().contains("Link")?
						"<a href=\"http://"+text+"\">"+text+"</a>":
						"<img src=\"http://"+text+"\"/>"; 
				descriptionArea.insert(textToInsert, descriptionArea.getCaretPosition());
				urlField.setText("");
				doLayout(false);
			}
		});
        cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				urlField.setText("");
				doLayout(false);
			}
		});
        doLayout(false);
    }
    private void doLayout(boolean insertURLPanel){
    	removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=gbc.gridy=0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        add(linkButton,gbc);
        gbc.gridx++;
        add(imageButton,gbc);
        gbc.gridwidth = 2;
        gbc.gridx=0;
        gbc.anchor = GridBagConstraints.CENTER;
        if(insertURLPanel){
            gbc.fill = GridBagConstraints.HORIZONTAL;
        	gbc.gridy++;
        	add(urlPanel,gbc);
        }
        gbc.gridy++;
        gbc.weightx=gbc.weighty=1;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(descriptionArea),gbc);
        revalidate();
    }
}
