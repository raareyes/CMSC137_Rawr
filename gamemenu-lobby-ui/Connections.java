import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/* 
 * This class should listen to the variable that holds the players
 * TODO: Do an enchancement on the data structures used. DS used might not be 
 * the most appropriate
 * TODO: Should have a listener to a variable that holds the data
*/
public class Connections extends JPanel {
  private boolean DEBUG = false;

  public Connections() {
    super(new GridLayout(1, 0));

    String[] columnNames = { "Player", "IP Address" };

    Object[][] data = {};

    final JTable table = new JTable(data, columnNames);
    table.setRowHeight(50);
    table.setPreferredScrollableViewportSize(new Dimension(500, 300));
    table.setFillsViewportHeight(true);

    if (DEBUG) {
      table.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          printDebugData(table);
        }
      });
    }

    //Create the scroll pane and add the table to it.
    JScrollPane scrollPane = new JScrollPane(table);

    this.setBackground(Color.BLACK);

    //Add the scroll pane to this panel.
    add(scrollPane);
  }

  private void printDebugData(JTable table) {
    int numRows = table.getRowCount();
    int numCols = table.getColumnCount();
    javax.swing.table.TableModel model = table.getModel();

    System.out.println("Value of data: ");
    for (int i = 0; i < numRows; i++) {
      System.out.print("    row " + i + ":");
      for (int j = 0; j < numCols; j++) {
        System.out.print("  " + model.getValueAt(i, j));
      }
      System.out.println();
    }
    System.out.println("--------------------------");
  }

  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  // private static void createAndShowGUI() {
  //   //Create and set up the window.
  //   JFrame frame = new JFrame("Connections");
  //   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  //   //Create and set up the content pane.
  //   Connections newContentPane = new Connections();
  //   newContentPane.setOpaque(true); //content panes must be opaque
  //   frame.setContentPane(newContentPane);

  //   //Display the window.
  //   frame.pack();
  //   frame.setVisible(true);
  // }

  private JPanel getPanel() {
    return this;
  }

  public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    // javax.swing.SwingUtilities.invokeLater(new Runnable() {
    //   public void run() {
    //     new Connections().getPanel();
    //   }
    // });
  }
}