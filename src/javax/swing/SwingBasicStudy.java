/*
 * 文件名：SwingBasicStudy.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SwingBasicStudy.java
 * 修改人：xiaofan
 * 修改时间：2017年3月7日
 * 修改内容：新增
 */
package javax.swing;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;

import javax.swing.UIManager.LookAndFeelInfo;

import junit.framework.TestCase;

/**
 * SwingBasic.
 * 
 * Swing组件按功能划分：
 * 
 * 顶层容器：JFrame、JApplet、JDialog、JWindow
 * 
 * 中间容器：Jpanel、JScrollPane、JSplitPane、JToolBar
 * 
 * 特殊容器：JInternalFrame、JLayeredPane、JRootPane、JDestopPane
 * 
 * 基本组件：实现人机交互，JButton、JComboBox、JList、JMenu、JSlider
 * 
 * 不可编辑信息的显示组件：JTable、JTextArea、JTextField
 * 
 * 特殊对话框组件：JColorChooser、JFileChooser
 * 
 * 
 * 
 * @author xiaofan
 */
public class SwingBasicStudy extends TestCase {
    /**
     * UIManager.
     * 
     * LAF:Look and Feel（界面外观）
     */
    public void testUIManager() {
        // 当前系统可用的LAF
        LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo info : lookAndFeels) {
            System.out.println(info.getName() + ":" + info);
        }
    }

    /**
     * Swing创建简单窗口应用.
     * 
     */
    public void testSwingComponent() {
        JFrame jFrame = new JFrame("zxiaofan.com");
        // 定义按钮，指定图标
        Icon okIcon = new ImageIcon("");
        JButton buttonOk = new JButton("OK", okIcon);
        // 定义单选按钮，初始状态选中
        JRadioButton radioMale = new JRadioButton("Male", true);
        // 定义单选按钮，初始状态未选中
        JRadioButton radiofeMale = new JRadioButton("FeMale", false);
        // 定义ButtonGroup，组合以上两个JRadioButton
        ButtonGroup bGroup = new ButtonGroup();
        // 定义复选框，初始状态选中
        JCheckBox checkBoxProgrammer = new JCheckBox("是否是程序员", true);
        String[] colors = new String[]{"Red", "Blue", "Green"};
        // 定义下拉选择框
        JComboBox<String> colorChooser = new JComboBox<>(colors);
        // 定义列表选择框
        JList<String> colorList = new JList<>(colors);
        // 定义3行8列的多行文本域
        JTextArea textArea = new JTextArea(3, 8);
        // 定义9列的单行文本域
        JTextField textFieldName = new JTextField(9);
        JMenuBar mBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuEdit = new JMenu("Edit");
        // 创建新建菜单选项，并制定图标
        Icon iconNew = new ImageIcon("");
        JMenuItem menuItemNew = new JMenuItem("New", iconNew);
        // 创建保存菜单选项
        JMenuItem menuItemSave = new JMenuItem("Save");
        // 创建退出菜单选项
        JMenuItem menuItemExit = new JMenuItem("Exit");
        JCheckBoxMenuItem autoWrap = new JCheckBoxMenuItem("AutoWrap");
        // 创建复制菜单选项
        JMenuItem menuItemCopy = new JMenuItem("Copy");
        // 创建粘贴菜单选项
        JMenuItem menuItemPaste = new JMenuItem("Paste");
        JMenu menuFormat = new JMenu("Format");
        JMenuItem menuItemComment = new JMenuItem("Comment"); // 注释选项
        JMenuItem menuItemCommentCancel = new JMenuItem("CommentCancel");
        // 定义右键菜单，用于设置程序风格
        JRadioButtonMenuItem itemMetal = new JRadioButtonMenuItem("Metal Style", true);
        JRadioButtonMenuItem itemNimbus = new JRadioButtonMenuItem("Nimbus Style");
        JRadioButtonMenuItem itemWindows = new JRadioButtonMenuItem("Windows Style");
        JRadioButtonMenuItem itemWindowsClassic = new JRadioButtonMenuItem("WindowsClassic Style");
        JRadioButtonMenuItem itemMotif = new JRadioButtonMenuItem("Motif Style");
        // init GUI
        //
        // 创建一个装载了文本框、按钮的JPanel
        JPanel jPanel = new JPanel();
        jPanel.add(textFieldName);
        jPanel.add(buttonOk);
        jFrame.add(jPanel, BorderLayout.SOUTH);
        // 创建装载了下拉选择框、3个JCheckBox的JPanel
        JPanel panelCheck = new JPanel();
        panelCheck.add(colorChooser);
        bGroup.add(radiofeMale);
        bGroup.add(radiofeMale);
        panelCheck.add(radiofeMale);
        panelCheck.add(radiofeMale);
        panelCheck.add(checkBoxProgrammer);
        // 创建垂直排列组件的Box，放置多行文本域Jpanel
        Box boxTopLeft = Box.createVerticalBox();
        // 使用JScrollPane作为普通组件的JViewPort
        JScrollPane scrollPane = new JScrollPane(textArea);
        boxTopLeft.add(scrollPane);
        boxTopLeft.add(panelCheck);
        // 创建水平排列组件的Box，放置boxTopLeft、
        Box boxTop = Box.createHorizontalBox();
        boxTop.add(boxTopLeft);
        boxTop.add(colorList);
        // 将boxTop添加到窗口中间
        jFrame.add(boxTop);

        // 组合菜单，并为菜单添加监听器
        // 为newItem设置快捷键，设置快捷键时需使用大写字母
        menuItemNew.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK));

    }
}
