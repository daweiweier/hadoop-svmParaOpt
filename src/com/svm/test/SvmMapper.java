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


		    String []arg ={"/home/kh/train.txt", //���SVMѵ��ģ���õ����ݵ�·��
	                "/home/kh/model_r.txt" //���SVMͨ��ѵ������ѵ��������ģ�͵�·��
			         ,g,c};  

		    String []parg={"/home/kh/test.txt",   //����Ǵ�Ų�������
		                   "/home/kh/model_r.txt",  //���õ���ѵ���Ժ��ģ��
		                   "/home/kh/out_r.txt"    //���ɵĽ�����ļ���·��
		                   }; 
	    
		    //����һ��ѵ������
		    svm_train t = new svm_train(); 
		    //����һ��Ԥ����߷���Ķ���
		    svm_predict p= new svm_predict(); 
		    t.main(arg);   //����
	    	acc = p.test(parg);  //����
	        context.write(key,  new Text(g + "," + c + "," + acc));
	      }
	}

}
