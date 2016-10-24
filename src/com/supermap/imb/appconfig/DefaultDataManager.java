package com.supermap.imb.appconfig;

import com.supermap.imb.base.R;

public class DefaultDataManager extends DataManager{

	public static final String mDefaultServer = "Changchun.smwu";
	
	/**
	 * ���캯��
	 */
	DefaultDataManager()
	{
		setWorkspaceServer(DefaultDataConfig.MapDataPath+mDefaultServer);
	}
	
	/**
	 * ��ȡ��ʾͼ����Դ
	 * @param name   ��ʾ���������
	 * @return       ��Դid
	 */
	public int getMapTypeResource(String name){
		if(name.equals("��ͼ�Ʒ���")){
			return R.drawable.ic_map_cloud;
		}else if(name.contains("Rest")){
			return R.drawable.ic_map_rest;
		}else if(name.equals("�ȸ��ͼ")){
			return R.drawable.ic_map_google;
		}else if(name.equals("�ٶȵ�ͼ")){
			return R.drawable.ic_map_baidu;
		}else if(name.equals("���ͼ")){
				return R.drawable.ic_map_tianditu;
		}else if(name.contains("SIT��ͼ")){
			return R.drawable.ic_map_sit;
		}else if(name.contains("DEM��ͼ")){
			return R.drawable.ic_map_dem;
		}else if(name.contains("SCI��ͼ")){
			return R.drawable.ic_map_sci;
		}else if(name.contains("CAD��ͼ")){
			return R.drawable.ic_map_cad;
		}else if(name.contains("iServerRest��ͼ")){
			return R.drawable.ic_map_rest;
		}else if(name.contains("WMS��ͼ")){
			return R.drawable.ic_map_wms;
		}else if(name.contains("Bing��ͼ")){
			return R.drawable.ic_map_bing;
		}else if(name.contains("OpenStreetMap")){
			return R.drawable.ic_map_osm;
		}
		return R.drawable.ic_map_vector;
	
	}
	
	
}
