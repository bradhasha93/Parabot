package InterfaceExplorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.LoopTask;
import org.rev317.api.methods.Interfaces;
import org.rev317.api.wrappers.hud.Interface;

@ScriptManifest(author = "Nade", description = "An Interface Explorer used to look through interfaces that are parents.<br><br><b>Created by Nade.</b>", name = "Interface Explorer", category =Category.UTILITY, version = 0.1, servers = { "" })
public class InterfaceExplorer extends Script implements Painter, LoopTask {
	

    private JFrame frame;
    private JList<String> list;
    private int selected;

    private void setupList() {
        try {
            list = new JList<>();

            list.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent event) {
                    String s = list.getSelectedValue();
                    String index = s.substring("Interface ".length());

                    if (getSelected() != Integer.parseInt(index)) {
                        setSelected(Integer.parseInt(index));

                        try {
                            final Object c = Interfaces.getOpenInterface();
                            final Field f = c.getClass().getDeclaredField("openInterfaceID");
                            f.setAccessible(true);
                           //f.set(Client.get().getInstance(), getSelected());
                        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException e) {
                        }
                    }
                }
            });

            List<String> found = new ArrayList<>();

               Interface widget = Interfaces.getParentInterface(239);
                if (widget != null) {
                    if (widget.getParentId() != -1 && widget.getParentId() != 0) {
                        if (!found.contains("Interface " + widget.getParentId())) {
                            System.out.println("Adding interface " + widget.getParentId() + ".");
                            found.add("Interface " + widget.getParentId());
                        }
                    }
            }

            String[] listData = (String[]) found.toArray(new String[found.size()]);

            try {
                Arrays.sort(listData, new Comparator<String>() {

                    @Override
                    public int compare(String s1, String s2) {
                        int i1 = Integer.parseInt(s1.substring("Interface ".length()));
                        int i2 = Integer.parseInt(s2.substring("Interface ".length()));
                        if (i1 > i2) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
            } catch (Exception e) {
            }

            getList().setListData(listData);
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("Exception " + e + " caught, attempting to give a fuck.... Failed.");
        }
    }

    private void setupFrame() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                setupList();

                frame = new JFrame("Interface Explorer");

                getFrame().setLocationByPlatform(true);
                getFrame().setPreferredSize(new Dimension(256, 512));
                getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                getFrame().setLayout(new BorderLayout());

                getFrame().addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent event) {
                    }
                });

                JLabel top = new JLabel("Select an interface to open.");
                JScrollPane scroll = new JScrollPane();
                scroll.setViewportView(getList());

                getFrame().add(top, BorderLayout.NORTH);
                getFrame().add(scroll, BorderLayout.CENTER);

                getFrame().pack();
                getFrame().setResizable(false);
                getFrame().setVisible(true);
            }
        });
    }

    @Override
    public boolean onExecute() {
        setupFrame();
        return true;
    }

    @Override
    public int loop() {
            if (getFrame() != null) {
                getFrame().setVisible(false);
                setFrame(null);
            }
        return 100;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JList<String> getList() {
        return list;
    }

    public void setList(JList<String> list) {
        this.list = list;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

	@Override
	public void paint(Graphics2D arg0, Object arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
}