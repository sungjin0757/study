package study.template.exercise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CalculatorV2 {

    public int sum(String path) throws IOException{
        ExerciseCallback callback=new ExerciseCallback() {
            @Override
            public int doSomething(BufferedReader br) throws IOException {
                int sum=0;
                while(true){
                    String temp=br.readLine();
                    if(temp==null)
                        break;
                    sum+=Integer.parseInt(temp);
                }
                return sum;
            }
        };

        return work(callback,path);
    }

    public int multiple(String path) throws IOException{
        ExerciseCallback callback=new ExerciseCallback() {
            @Override
            public int doSomething(BufferedReader br) throws IOException {
                int sum=1;
                while(true){
                    String temp=br.readLine();
                    if(temp==null)
                        break;
                    sum*=Integer.parseInt(temp);
                }
                return sum;
            }
        };

        return work(callback,path);
    }

    public int work(ExerciseCallback exerciseCallback,String path) throws IOException{
        BufferedReader br=null;
        try{
            br=new BufferedReader(new FileReader(path));
            int sum=exerciseCallback.doSomething(br);
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
