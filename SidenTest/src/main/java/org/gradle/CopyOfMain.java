package org.gradle;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CopyOfMain {

	public static void main(String[] args) {
		doPost("http://3ds.pokemon-gl.com/frontendApi/gbu/getSeasonPokemonDetail");
	}


	private static void doPost(String url) {
		
		String requestJSON = "languageId=1&seasonId=111&battleType=1&timezone=JST&pokemonId=115-0&displayNumberWaza=10&displayNumberTokusei=3&displayNumberSeikaku=10&displayNumberItem=10&displayNumberLevel=10&displayNumberPokemonIn=10&displayNumberPokemonDown=10&displayNumberPokemonDownWaza=10&timeStamp=1437715953564";

		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setFixedLengthStreamingMode(requestJSON.getBytes().length);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setRequestProperty("Referer", "http://3ds.pokemon-gl.com/battle/");
		
			
			System.out.println(conn.toString());
			conn.connect();

			DataOutputStream os = new DataOutputStream(conn.getOutputStream());
			os.write(requestJSON.getBytes("UTF-8"));
			os.flush();
			os.close();

			if( conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
				StringBuffer responseJSON = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				while ((inputLine = reader.readLine()) != null) {
					responseJSON.append(inputLine);
				}
				System.out.println("doPost success");
				System.out.println(responseJSON);
			}else{
				System.out.println("doPost failed");
			}
		}catch(IOException e){
			System.out.println("IOException");
		}finally {
			if( conn != null ){
				conn.disconnect();
			}
		}
	}

}
