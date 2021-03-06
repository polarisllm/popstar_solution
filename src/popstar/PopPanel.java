/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package popstar;

import GameLogic.GameState;
import static GameLogic.GameState.MAP_SIZE;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author wssccc
 */
public class PopPanel extends javax.swing.JPanel {

    /**
     * Creates new form PopPanel
     */
    GameState gs;
    int color = 0;
    boolean edit = true;

    public PopPanel() {
        initComponents();
    }
    static final Color[] mapping = new Color[]{Color.gray, Color.red, Color.yellow, Color.blue, Color.green, Color.magenta};

    public void display() {
        int vboxSize = this.getHeight() / GameState.MAP_SIZE;
        int hboxSize = this.getWidth() / GameState.MAP_SIZE;
        Graphics g = this.getGraphics();

        for (int i = 0; i < GameState.MAP_SIZE; i++) {
            for (int j = 0; j < GameState.MAP_SIZE; j++) {
                g.setColor(mapping[gs.mdata[  i * MAP_SIZE + j]]);
                g.fillRect(j * hboxSize, i * vboxSize, hboxSize - 1, vboxSize - 1);
            }

        }
        g.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 410, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 345, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed

        int vboxSize = this.getHeight() / GameState.MAP_SIZE;
        int hboxSize = this.getWidth() / GameState.MAP_SIZE;
        int x = evt.getX() / hboxSize;
        int y = evt.getY() / vboxSize;
        if (edit) {
            gs.mdata[ y * MAP_SIZE + x] = (byte) color;
        } else {
            gs.tap(x, y);
        }
        display();

    }//GEN-LAST:event_formMousePressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
