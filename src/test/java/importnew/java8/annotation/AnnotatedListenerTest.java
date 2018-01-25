package importnew.java8.annotation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AnnotatedListenerTest {

	public static void main(String[] args) {
		new SwingProWithAnnotaion().init();
	}

	static class SwingProWithAnnotaion {
		private JFrame mainWin = new JFrame("使用注解绑定事件监听器");

		@ActionListenerFor(OkListener.class)
		private JButton ok = new JButton("确定");
		@ActionListenerFor(CancelListener.class)
		private JButton cancel = new JButton("取消");

		public void init() {
			JPanel jp = new JPanel();

			try {
				install(this);
			} catch (IllegalAccessException | InstantiationException e) {
				e.printStackTrace(System.out);
			}

			jp.add(ok);
			jp.add(cancel);
			mainWin.add(jp);
			mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWin.pack();
			mainWin.setVisible(true);
		}

		public static void install(Object targetObject) throws IllegalAccessException, InstantiationException {
			for (Field field : targetObject.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(ActionListenerFor.class)) {
					field.setAccessible(true);
					AbstractButton targetButton = (AbstractButton) field.get(targetObject);
					Class<? extends ActionListener> listener = field.getAnnotation(ActionListenerFor.class).value();
					targetButton.addActionListener(listener.newInstance());
				}
			}
		}

	}

	@Inherited
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ActionListenerFor {
		Class<? extends ActionListener> value();
	}

	static class SwingPro {
		private JFrame mainWin = new JFrame("使用注解绑定事件监听器");

		private JButton ok = new JButton("确定");
		private JButton cancel = new JButton("取消");

		public void init() {
			JPanel jp = new JPanel();

			ok.addActionListener(new OkListener());
			cancel.addActionListener(new CancelListener());

			jp.add(ok);
			jp.add(cancel);
			mainWin.add(jp);
			mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainWin.pack();
			mainWin.setVisible(true);
		}
	}

	static class OkListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Ok is clicked");
		}
	}

	static class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "Cancel is clicked");
		}
	}

}
