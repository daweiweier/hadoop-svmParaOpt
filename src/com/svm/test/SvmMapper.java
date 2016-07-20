package com.svm.test;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import service.svm_predict;
import service.svm_train;

public class SvmMapper extends Mapper<LongWritable, Text, LongWritable, Text>{
	
	double acc = 0;
	protected void map(LongWritable key, Text value,Context context)
			throws IOException, InterruptedException {
		
	      StringTokenizer itr = new StringTokenizer(value.toString());
	      while (itr.hasMoreTokens()) {
	      String[] param = itr.nextToken().split(",");
	      String g = param[0];
	      String c = param[1];


		    String []arg ={"/home/kh/train.txt", //存放SVM训练模型用的数据的路径
	                "/home/kh/model_r.txt" //存放SVM通过训练数据训练出来的模型的路径
			         ,g,c};  

		    String []parg={"/home/kh/test.txt",   //这个是存放测试数据
		                   "/home/kh/model_r.txt",  //调用的是训练以后的模型
		                   "/home/kh/out_r.txt"    //生成的结果的文件的路径
		                   }; 
	    
		    //创建一个训练对象
		    svm_train t = new svm_train(); 
		    //创建一个预测或者分类的对象
		    svm_predict p= new svm_predict(); 
		    t.main(arg);   //调用
	    	acc = p.test(parg);  //调用
	        context.write(key,  new Text(g + "," + c + "," + acc));
	      }
	}

}
