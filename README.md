# HttpUtil

## Introduce

An util to create http connection by POST

Copying this jar file to your lib directory of project, and adding dependency in gradle file.

----

## Example

	new HttpUtil().setUrl("https://github.com/13608089849/")
			.setConnectTimeOut(5000)
			.setReadTimeOut(5000)
			.addParams("key1","value1")
			.addParams("key2","value2")
			.execute(new CallBack(){
				@Override
				public void onSuccess(String string) {
					Log.e("onSuccess", string);
				}

				@Override
				public void onFailure(String string, Exception e) {
					Log.e("String", string);
					if (e!=null){
						e.printStackTrace();
					}
				}

				@Override
				public void onBefore(String string, JSONObject jsonObject){
					Log.e("Url", string);
					Log.e("Params", jsonObject.toString());
				｝
			});