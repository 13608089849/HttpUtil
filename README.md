# HttpUtil

## Introduce

An util to create http connection by POST

Copying this jar file to your lib directory of project, and adding dependency.

----

## Example

	HttpUtil().setUrl("https://github.com/13608089849/")
			.setConnectTimeOut(5000)
			.setReadTimeOut(5000)
			.addParams("key1","value1")
			.addParams("key2","value2")
			.execute(new HttpCallback(){
				@Override
				public void onSuccess(String s) {
					Log.e("onSuccess", s);
				}

				@Override
				public void onFailure(Exception e) {
					e.printStackTrace();
				}

				@Override
				public void onBefore(String string, JSONObject jsonObject){
					super.onBefore(url, params);
				｝
			});
