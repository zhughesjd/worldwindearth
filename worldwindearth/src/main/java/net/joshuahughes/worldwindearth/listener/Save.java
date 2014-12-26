package net.joshuahughes.worldwindearth.listener;

import java.awt.event.ActionEvent;

public enum Save implements Listener{
    Save_to_My_Places{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(this.name());
        }
    },
    Save_Place_As___{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(this.name());
        }
    },Save_My_Places{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(this.name());
        }
    },Save_Image___{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(this.name());
        }
    }
}
