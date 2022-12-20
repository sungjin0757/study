package study.template.exercise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CalculatorV3 {

    public int sum(String path) throws IOException{
        ExerciseCallbackV2<Integer> callback=new ExerciseCallbackV2<Integer>() {
            @Override
            public Integer doSomething(int medium,Integer res) throws IOException {
                return res+medium;
            }
        };

        return work(callback,path,0);
    }

    public int multiple(String path) throws IOException{
        ExerciseCallbackV2<Integer> callback=new ExerciseCallbackV2<Integer>() {
            @Override
            public Integer doSomething(int medium,Integer res) throws IOException {
                return res*medium;
            }
        };

        return work(callback,path,1);
    }

    public <T> T work(ExerciseCallbackV2<T> exerciseCallback,String path,T initVal) throws IOException{
        BufferedReader br=null;
        try{
            br=new BufferedReader(new FileReader(path));
            T res=initVal;
            while(true){
                String temp=br.readLine();
                if(temp==null)
                    break;
                res=exerciseCallback.doSomething(Integer.parseInt(temp),res);
            }
            return res;
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
