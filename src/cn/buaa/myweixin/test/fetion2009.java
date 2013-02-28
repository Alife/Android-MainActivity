package cn.buaa.myweixin.test;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 测试动态使用android控件
 * 
 * @author gaolei by 20090827
 */
public class fetion2009 extends Activity {
	/** Called when the activity is first created. */
	ProgressBar pb; // 进度条控件，但拿出来是为了可控，动态改变其进度
	// 聊天对话的底色是间隔的
	private static final int[] bg = { Color.WHITE, Color.GRAY };
	private static int bgIndex = 0; // 聊天对话的底色 当前色应该是bg中的索引值

	// 以下 布局参数
	// 标识当前控件的宽高情况FILL_PARENT=占据全部父控件，WRAP_CONTENT=仅包裹控件中的内容//还有其他作用比如左右边距，这里我们使用默认的
	private LinearLayout.LayoutParams LP_FF = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.FILL_PARENT);
	private LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
			LayoutParams.WRAP_CONTENT);
	private LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
			LayoutParams.WRAP_CONTENT);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 聊天对白窗口需要滚动
		ScrollView sv = new ScrollView(this);
		sv.setLayoutParams(LP_FF);

		LinearLayout layout = new LinearLayout(this); // 线性布局方式
		layout.setOrientation(LinearLayout.VERTICAL); // 控件对其方式为垂直排列
		layout.setBackgroundColor(0xff00ffff); // 设置布局板的一个特殊颜色，这可以检验我们会话时候是否有地方颜色不正确！

		// 丰富聊天页面，也顺带测试页面滚动效果，增加了10个重复的对话内容
		for (int i = 0; i < 10; i++) {
			setSendMsg(layout, this, getCurrColor(), i + "聊天内容在这里。。");
		}

		// 发送文件效果1，圆环进度条，也是ProgressBar默认的效果
		setSendFile(layout, this, getCurrColor(), "我的照片.jpg");

		// 发送文件效果 2,矩行进度条，也是ProgressBar的风格设置成
		// style="?android:attr/progressBarStyleHorizontal"的效果
		setSendFile2(layout, this, getCurrColor(), "我的照片.jpg");

		for (int i = 0; i < 10; i++) {
			setSendMsg(layout, this, getCurrColor(), i + "聊天内容在这里。。");
		}
		sv.addView(layout); // 把线性布局加入到ScrollView中
		setContentView(sv); // 设置当前的页面为ScrollView
	}

	/**
	 * 获取当前聊天对白的底色值
	 * 
	 * @return 当前聊天对白的底色值
	 */
	private int getCurrColor() {
		return bg[(++bgIndex) % bg.length];
	}

	/**
	 * 动态增加一个聊天内容 这里为了简化编程把 某人说 和 内容放到一个TextView中，可以根据设计文档拆成2个TextView分别显示，设置字体等
	 * 
	 * @param layout
	 *            TextView 控件欲添加到的目标layout
	 * @param context
	 *            构建View控件的必须参数 既View控件的环境
	 * @param bgColur
	 *            TextView 控件的背景色
	 * @param MSG
	 *            TextView 控件要现实的文本内容
	 */
	private void setSendMsg(LinearLayout layout, Context context, int bgColur, String MSG) {
		TextView tv = new TextView(context); // 普通聊天对话
		// 获取一个全局的日历实例，用于获取当前系统时间并格式化成小时：分钟形式，仅用于测试，这里的时间应该是由其他程序提供
		tv.setText("某人  说: [" + DateFormat.format("kk:mm", Calendar.getInstance()) + "]\n" + MSG);
		tv.setBackgroundColor(bgColur);
		layout.addView(tv);
	}

	/**
	 * 动态增加一个发送文件的会话条目 这里因为是发送进度条与取消按钮的水平对其方式，所以需要增加一个LinearLayout
	 * 
	 * @param layout
	 *            欲添加到的目标layout
	 * @param context
	 *            构建 View控件的必须参数 既View控件的环境
	 * @param bgColur
	 *            控件的背景色
	 * @param MSG
	 *            控件要现实的文本内容
	 */
	private void setSendFile(LinearLayout layout, Context context, int bgColur, String fileName) {
		// 把 某人说 [时间]
		// 要发送的文件信息 全都交给 setSendMsg 绘制吧!
		setSendMsg(layout, context, bgColur, "正在发送" + fileName);
		// 水平排列2个控件需要一个LinearLayout，排列方式默认的就是水平排列
		LinearLayout myLayout = new LinearLayout(context);
		// 这个 LinearLayout控件的背景色需要设置，要不就会显示出主LinearLayout的颜色了，即0xff00ffff
		myLayout.setBackgroundColor(bgColur);

		// 动态创建一个 ProgressBar，以默认属性加入到myLayout中
		ProgressBar pb = new ProgressBar(context);
		pb.setLayoutParams(LP_WW);
		myLayout.addView(pb);

		// 动态创建一个 Button，以默认属性加入到myLayout中
		Button bt = new Button(context);
		bt.setLayoutParams(LP_WW);
		bt.setText(" 取消");
		myLayout.addView(bt);
		// 将水平布局的 LinearLayout及其内如所有控件添加到主layout中
		layout.addView(myLayout);
	}

	/**
	 * 动态增加一个发送文件的会话条目 但为了保障ProgressBar和
	 * Button的底色符合设计要求，增加了一个LinearLayout，并设置其背景色
	 * 
	 * @param layout
	 *            欲添加到的目标layout
	 * @param context
	 *            构建 View控件的必须参数 既View控件的环境
	 * @param bgColur
	 *            控件的背景色
	 * @param MSG
	 *            控件要现实的文本内容
	 */
	private void setSendFile2(LinearLayout layout, Context context, int bgColur, String fileName) {
		setSendMsg(layout, context, bgColur, "正在发送" + fileName);

		LinearLayout myLayout = new LinearLayout(context);
		myLayout.setBackgroundColor(bgColur);
		myLayout.setOrientation(LinearLayout.VERTICAL);// 控件对其方式为垂直，默认为水平

		// ProgressBar 的默认风格是圆环型，这里需要设置她的风格为Horizontal(水平线)
		pb = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
		pb.setLayoutParams(LP_FW);
		pb.setProgress(45); // 设置第1进度为45
		pb.setSecondaryProgress(0); // 这里我们不需要第2进度，所以为0
		myLayout.addView(pb);

		Button bt = new Button(context);
		bt.setLayoutParams(LP_WW);
		bt.setText("取消");
		myLayout.addView(bt);

		layout.addView(myLayout);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("onKeyDown:", " keyCode=" + keyCode + " KeyEvent=" + event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:

			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:

			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			// 右左按键可以控制第一进度的增减
			pb.setProgress(pb.getProgress() - 5);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			pb.setProgress(pb.getProgress() + 5);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:

			break;
		case KeyEvent.KEYCODE_0:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
