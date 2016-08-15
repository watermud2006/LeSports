/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zblibrary.demo.activity_fragment;

import zblibrary.demo.DEMO.DemoListActivity;
import zblibrary.demo.DEMO.DemoTabActivity;
import zblibrary.demo.R;
import zblibrary.demo.DEMO.DemoFragment;
import zblibrary.demo.DEMO.DemoTabFragment;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.ui.PlacePickerWindow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**应用主页
 * @author Lemon
 * @use BottomTabActivity.createIntent(...)
 */
public class BottomTabActivity extends BaseActivity  {
	private static final String TAG = "BottomTabActivity";


	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**启动这个Activity的Intent
	 * @param context
	 * @return
	 */
	public static Intent createIntent(Context context) {
		return new Intent(context, BottomTabActivity.class);
	}


	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	@Override
	@NonNull
	public BaseActivity getActivity() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_tab_activity);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initListener();
		//功能归类分区方法，必须调用>>>>>>>>>>
	}



	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@SuppressWarnings("unused")
	private View rlBottomTabTopbar;
	private TextView tvBottomTabTitle;	

	private View[] llBottomTabTabs;
	private ImageView[] ivBottomTabTabs;
	private TextView[] tvBottomTabTabs;

	@Override
	public void initView() {// 必须调用
		exitAnim = R.anim.bottom_push_out;

		rlBottomTabTopbar = findViewById(R.id.rlBottomTabTopbar);
		tvBottomTabTitle = (TextView) findViewById(R.id.tvBottomTabTitle);

		llBottomTabTabs = new View[4];
		llBottomTabTabs[0] = findViewById(R.id.llBottomTabTab0);
		llBottomTabTabs[1] = findViewById(R.id.llBottomTabTab1);
		llBottomTabTabs[2] = findViewById(R.id.llBottomTabTab2);
		llBottomTabTabs[3] = findViewById(R.id.llBottomTabTab3);

		ivBottomTabTabs = new ImageView[4];
		ivBottomTabTabs[0] = (ImageView) findViewById(R.id.ivBottomTabTab0);
		ivBottomTabTabs[1] = (ImageView) findViewById(R.id.ivBottomTabTab1);
		ivBottomTabTabs[2] = (ImageView) findViewById(R.id.ivBottomTabTab2);
		ivBottomTabTabs[3] = (ImageView) findViewById(R.id.ivBottomTabTab3);

		tvBottomTabTabs = new TextView[4];
		tvBottomTabTabs[0] = (TextView) findViewById(R.id.tvBottomTabTab0);
		tvBottomTabTabs[1] = (TextView) findViewById(R.id.tvBottomTabTab1);
		tvBottomTabTabs[2] = (TextView) findViewById(R.id.tvBottomTabTab2);
		tvBottomTabTabs[3] = (TextView) findViewById(R.id.tvBottomTabTab3);

	}

	/**获取新的Fragment
	 * @param position
	 * @return
	 */
	protected Fragment getFragment(int position) {
		bundle = new Bundle();
		switch (position) {
		case 1:
			return new DemoFragment();
		case 2:
			return new DemoTabFragment();
		case 3:
			return new SettingFragment();
		default:
			UserListFragment fragment = new UserListFragment();
			bundle.putInt(UserListFragment.ARGUMENT_RANGE, UserListFragment.RANGE_ALL);
			return fragment;
		}
	};


	private static final String[] TABS = {"活动", "消息", "场馆", "我的"};
	private static final int[][] TAB_IMAGE_RES_IDS = {
		{R.drawable.earth_light, R.drawable.earth},
		{R.drawable.mail_light, R.drawable.mail},
		{R.drawable.search_light, R.drawable.search},
		{R.drawable.setting_light, R.drawable.setting}
	};

	protected int currentPosition = 0;
	/**选择并显示fragment
	 * @param position
	 */
	public void selectFragment(int position) {
		if (currentPosition == position) {
			if (fragments[position] != null && fragments[position].isVisible()) {
				Log.e(TAG, "selectFragment currentPosition == position" +
						" >> fragments[position] != null && fragments[position].isVisible()" +
						" >> return;	");
				return;
			}
		}

		//导致切换时闪屏，建议去掉BottomTabActivity中的topbar，在fragment中显示topbar
		//		rlBottomTabTopbar.setVisibility(position == 2 ? View.GONE : View.VISIBLE);

		tvBottomTabTitle.setText(TABS[position]);

		for (int i = 0; i < llBottomTabTabs.length; i++) {
			ivBottomTabTabs[i].setImageResource(TAB_IMAGE_RES_IDS[i][i == position ? 1 : 0]);
			tvBottomTabTabs[i].setTextColor(getResources().getColor(i == position ? R.color.white : R.color.black));
		}

		if (fragments[position] == null) {
			fragments[position] = getFragment(position);
		}

		// 用全局的fragmentTransaction因为already committed 崩溃
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.hide(fragments[currentPosition]);
		if (fragments[position].isAdded() == false) {
			fragmentTransaction.add(R.id.flBottomTabFragmentContainer, fragments[position]);
		}
		fragmentTransaction.show(fragments[position]).commit();

		this.currentPosition = position;
	};


	// UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private Fragment[] fragments;
	@Override
	public void initData() {// 必须调用


		// fragmentActivity子界面初始化<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		fragments = new Fragment[TABS.length];
		selectFragment(currentPosition);

		// fragmentActivity子界面初始化>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	}



	// data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	// listener事件监听区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	private static final int REQUEST_TO_PLACE_PICKER = 10;

	public void onClick(View v) {
		Toast.makeText(context, "点击事件", Toast.LENGTH_LONG).show();
		switch (v.getId()) {
			case R.id.tvDemoTabLeft:
				toActivity(PlacePickerWindow.createIntent(context, context.getPackageName(), 2)
						, REQUEST_TO_PLACE_PICKER, false);
				break;
			case R.id.tvDemoTabRight:
				toActivity(DemoListActivity.createIntent(context, 0).putExtra(DemoTabActivity.INTENT_TITLE, "筛选"));
				break;
			default:
		}
	}
	@Override
	public void initListener() {// 必须调用

		for (int i = 0; i < llBottomTabTabs.length; i++) {
			final int which = i;
			llBottomTabTabs[which].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectFragment(which);
				}
			});
		}
	}

	// 系统自带监听方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	private long firstTime = 0;//第一次返回按钮计时
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			long secondTime = System.currentTimeMillis();
			if(secondTime - firstTime > 2000){
				showShortToast("再按一次退出");
				firstTime = secondTime;
			} else {//完全退出
				moveTaskToBack(false);//应用退到后台
				System.exit(0);
			}
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	// 类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case REQUEST_TO_PLACE_PICKER:
				Toast.makeText(context, "返回结果", Toast.LENGTH_LONG).show();
				ArrayList<String> placeList = data.getStringArrayListExtra(PlacePickerWindow.RESULT_PLACE_LIST);
				break;
			default:
		}
	}
	// 类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	// listener事件监听区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>



	// 内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	// 内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}