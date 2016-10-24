package com.supermap.imb.appconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import com.supermp.imb.file.Decompressor;
import com.supermp.imb.file.FileManager;
import com.supermp.imb.file.MyAssetManager;

public class DefaultDataConfig {

	private final String MapData = "MapData";
	public static final String MapDataPath = MyApplication.SDCARD+"SuperMap/Demos/Data/BaseDemo/";
	public static final String LicPath = MyApplication.SDCARD+"SuperMap/License/";
	private final String LicName = "Trial License.slm";
	
	/**
	 * ���캯��
	 */
	public DefaultDataConfig()
	{
		
	}
	
    /**
     * ��������
     */
	public void autoConfig(){
		//���������������Ϊ�û��Ѿ�����������
		String mapDataPah = MapDataPath+"/";
	    String license    = LicPath + LicName;
		
		File licenseFile = new File(license);
		if(!licenseFile.exists())
			configLic();
		
		File dir = new File(mapDataPah);
		if(!dir.exists()){
			FileManager.getInstance().mkdirs(mapDataPah);
			configMapData();
		}else{
			if(FileManager.getInstance().isFileExsit(mapDataPah+DefaultDataManager.mDefaultServer)){
				return;
			}
			
			boolean hasMapData = false;
			File[] datas = dir.listFiles();
			for(File data:datas){
				if(data.getName().endsWith("SMWU")||data.getName().endsWith("smwu")
					||data.getName().endsWith("SXWU")||data.getName().endsWith("sxwu"))
				{
					//���Ĭ�ϵ����ݱ�ɾ�����Ǿͼ��ص�һ�������ռ�
					MyApplication.getInstance().getDefaultDataManager().setWorkspaceServer(data.getAbsolutePath());
					hasMapData = true;
					break;
				}
			}
			if(!hasMapData)
			{
				configMapData();
			}
		}
	}
	
	/**
	 * ��������ļ�
	 */
	private void configLic()
	{
		InputStream is = MyAssetManager.getInstance().open(LicName);
		if(is!=null)
		   FileManager.getInstance().copy(is, LicPath+LicName);
	}

	/**
	 * ���õ�ͼ����
	 */
	private void configMapData(){
		String[] datas = MyAssetManager.getInstance().opendDir(MapData);
		for(String data:datas){
			InputStream is = MyAssetManager.getInstance().open(MapData+"/"+data);
			String zip = MapDataPath+"/"+data;
			boolean result = FileManager.getInstance().copy(is, zip);
			if(result){
					Decompressor.UnZipFolder(zip, MapDataPath);
					//ɾ��ѹ����
					File zipFile = new File(zip);
					zipFile.delete();
			}
		}
		
		
	}
}
