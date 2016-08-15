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

package zblibrary.demo.DEMO;

import java.util.ArrayList;
import java.util.List;

import zblibrary.demo.R;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.bean.Entry;
import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.ui.PageScroller;
import zuo.biao.library.util.StringUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**使用方法：复制>粘贴>改名>改代码  */
/**activity示例
 * @author Lemon
 * @warn 复制到其它工程内使用时务必修改import R文件路径为所在应用包名
 * @use toActivity(DemoActivity.createIntent(...));
 */
public class DemoActivity extends BaseActivity implements OnClickListener, OnBottomDragListener {
	private static final String TAG = "DemoActivity";

	//启动方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	public static final String INTENT_USER_ID = "INTENT_USER_ID";

	public static final String RESULT_CLICKED_ITEM = "RESULT_CLICKED_ITEM";

	/**启动这个Activity的Intent
	 * @param context
	 * @param userId
	 * @return
	 */
	public static Intent createIntent(Context context, long userId) {
		return new Intent(context, DemoActivity.class).putExtra(INTENT_USER_ID, userId);
	}

	//启动方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	

	@Override
	@NonNull
	public BaseActivity getActivity() {
		return this;
	}

	private long userId = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO demo_activity改为你所需要的layout文件；传this是为了底部左右滑动手势
		setContentView(R.layout.demo_activity, this);

		intent = getIntent();
		userId = intent.getLongExtra(INTENT_USER_ID, userId);

		//功能归类分区方法，必须调用<<<<<<<<<<
		initView();
		initData();
		initListener();
		//功能归类分区方法，必须调用>>>>>>>>>>

		Toast.makeText(context, "这是一个分页列表，中速滑动直接滚动一页。\n如果不需要则把PageScroller相关代码去掉"
				, Toast.LENGTH_LONG).show();
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//示例代码<<<<<<<<
	private TextView tvDemoTitle;
	private ListView lvDemo;
	//示例代码>>>>>>>>
	@Override
	public void initView() {//必须在onCreate方法内调用

		//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		tvDemoTitle = (TextView) findViewById(R.id.tvDemoTitle);

		lvDemo = (ListView) findViewById(R.id.lvDemo);

		//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}

	//示例代码<<<<<<<<
	private DemoAdapter2 adapter;
	//示例代码>>>>>>>>
	/** 示例方法 ：显示列表内容
	 * @author author
	 * @param list
	 */
	private void setList(List<Entry<String, String>> list) {
		if (list == null || list.size() <= 0) {
			Log.i(TAG, "setList list == null || list.size() <= 0 >> lvDemo.setAdapter(null); return;");
			adapter = null;
			lvDemo.setAdapter(null);
			return;
		}

		if (adapter == null) {
			adapter = new DemoAdapter2(context, list);
			lvDemo.setAdapter(adapter);
		} else {
			adapter.refresh(list);
		}
	}


	//UI显示区(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>










	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	//示例代码<<<<<<<<
	private List<Entry<String, String>> list;
	//示例代码>>>>>>>>>
	@Override
	public void initData() {//必须在onCreate方法内调用

		//示例代码<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

		if (StringUtil.isNotEmpty(getIntent().getStringExtra(INTENT_TITLE), false)) {
			tvDemoTitle.setText(StringUtil.getCurrentString());
		}

		showProgressDialog(R.string.loading);

		runThread(TAG + "initData", new Runnable() {
			@Override
			public void run() {

				list = getList(userId);
				runUiThread(new Runnable() {
					@Override
					public void run() {
						dismissProgressDialog();
						setList(list);
					}
				});
			}
		});

		//示例代码>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	}


	/**示例方法：获取列表
	 * @author Lemon
	 * @param userId
	 * @return
	 */
	protected List<Entry<String, String>> getList(long userId) {
		List<Entry<String, String>> list = new ArrayList<Entry<String, String>>();
		for (int i = 0; i < 64; i++) {
			list.add(new Entry<String, String>("联系人" + i + userId, String.valueOf(1311736568 + i*i + userId)));
		}
		return list;
	}

	/**示例方法：添加列表
	 * @author Lemon
	 */
	private void addList() {
		int formerCout = adapter == null ? 0 : adapter.getCount() - 1;

		if (list == null) {
			list = new ArrayList<>();
		}
		userId += list.size();
		list.addAll(getList(userId));
		
		if (adapter != null) {
			adapter.refresh(list);
		}
		lvDemo.smoothScrollToPosition(formerCout);		
	}

	//Data数据区(存在数据获取或处理代码，但不存在事件监听代码)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//Listener事件监听区(只要存在事件监听代码就是)<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	@Override
	public void initListener() {//必须在onCreate方法内调用
		//示例代码<<<<<<<<<<<<<<<<<<<
		findViewById(R.id.ivDemoReturn).setOnClickListener(this);
		findViewById(R.id.tvDemoForward).setOnClickListener(this);

		lvDemo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setResult(RESULT_OK, new Intent().putExtra(RESULT_CLICKED_ITEM, position));
				finish();
			}
		});
		
		//分页滚动示例代码<<<<<<<<<<<<<<<<<<<
		new PageScroller(lvDemo).init();
		//分页滚动示例代码>>>>>>>>>>>>>>>>>>>

		//示例代码>>>>>>>>>>>>>>>>>>>
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		if (rightToLeft) {
			addList();

			return;
		}	

		finish();
	}


	//系统自带监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	
	//示例代码<<<<<<<<<<<<<<<<<<<
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivDemoReturn:
			onDragBottom(false);
			break;
		case R.id.tvDemoForward:
			onDragBottom(true);
			break;
		default:
			break;
		}
	}
	//示例代码>>>>>>>>>>>>>>>>>>>


	//类相关监听<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<





	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听方法>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//类相关监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	//系统自带监听>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


	//Listener事件监听区(只要存在事件监听代码就是)>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>








	//内部类,尽量少用<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



	//内部类,尽量少用>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

}