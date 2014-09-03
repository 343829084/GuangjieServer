/**
 * 
 */
package com.taobao.guangjie.dl;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * mongodb访问类
 * 
 * 单例，其他地方不要自己去连接数据库，都通过
 * 这里进行连接，这样可以在整个系统共享同一个
 * mongodb连接池
 */
public class MongoDL {
	public static final String IP = "121.199.0.177";
	public static final String DB_NAME = "doge";

	private static MongoDL mongoDL = null;
	private MongoClient mclient = null;
	private DB mdb = null;
	private static Logger logger = LoggerFactory.getLogger(MongoDL.class);

    //staic块，只初始化一次，在类加载的时候首先执行
	static {
		try {
			mongoDL = new MongoDL();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("mongodb加载失败");
		}
	}

	public static MongoDL getInstance() throws UnknownHostException {
		if (mongoDL == null) {
			mongoDL = new MongoDL();
		}
		return mongoDL;
	}

	private MongoDL() throws UnknownHostException {
		logger.info("mongodb创建");
		connect(IP, DB_NAME);
	}

	private void connect(String server, int port, String dbName)
			throws UnknownHostException {
		mclient = new MongoClient(server, port);
		mdb = mclient.getDB(dbName);
		if (mclient != null && mdb != null) {
			logger.info("mongodb连接成功");
		} else {
			logger.error("mongodb连接失败");
		}
		
	}

	private void connect(String server, String dbName)
			throws UnknownHostException {
		connect(server, 27017, dbName);
	}

	public MongoClient getMclient() {
		return mclient;
	}

	public DB getMdb() {
		return mdb;
	}

}
