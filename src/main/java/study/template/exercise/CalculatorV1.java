package study.template.exercise;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class CalculatorV1 {

    public int sum(String path) throws IOException{
        BufferedReader br=null;

        try{
            br=new BufferedReader(new FileReader(path));
            int sum=0;
            while(true){
                String temp=br.readLine();
                if(temp==null)
                    break;
                sum+=Integer.parseInt(temp);
            }

            return sum;

        }catch (IOException e){
            log.info(e.getMessage());
            throw e;
        }finally {
            if(br!=null){
                try{
                    br.close();
                }catch (IOException e){
                    log.info(e.getMessage());
                }
            }
        }
    }
}
