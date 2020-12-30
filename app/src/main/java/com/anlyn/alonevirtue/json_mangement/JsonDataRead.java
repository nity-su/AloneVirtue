package com.anlyn.alonevirtue.json_mangement;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonDataRead {
    public String read(Context context, String fileName) {
        try {
            //FileInputStream 파일 -> 바이트 스트림
            //openFileInput에 반환 값은 openFileInput() FileInputStream이다
            FileInputStream fis = new FileInputStream(new File(fileName));
            //바이트 stream -(지정된 charset를 사용)> 문자 stream
            //InputStream은 영어와 숫자만 표현 가능하므로 한글이나 다른 문자의 경우엔 유니코드가 포함된 InputStreamReader를 사용한다.
            InputStreamReader isr = new InputStreamReader(fis);
            //입출력 속도 상승을 위해 사용
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            //ufferedReader.readLine()은 Stream에서 한줄을 읽어 반환한다.
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }
}
