package cn.buaa.myweixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Appstart extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN); //ȫ����ʾ
		// Toast.makeText(getApplicationContext(), "���ӣ��úñ��У�",
		// Toast.LENGTH_LONG).show();
		// overridePendingTransition(R.anim.hyperspace_in,
		// R.anim.hyperspace_out);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// ֱ�ӽ�����ҳ
				// Intent intent = new Intent(Appstart.this, Welcome.class);
				// startActivity(intent);

				// �ж��Ƿ��ǵ�һ�ν���
				// ��һ�ν�����ʾ���ܽ���,����ֱ�ӽ�����ҳ
				boolean isfirst = false;
				if (isfirst) {
					Intent intent = new Intent(Appstart.this, Whatsnew.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(Appstart.this, MainWeixin.class);
					startActivity(intent);
				}

				Appstart.this.finish();
			}
		}, 1000);
	}
}