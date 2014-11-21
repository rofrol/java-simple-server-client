import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Session {
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			println("Are you [h]osting or [c]onnecting?");
			boolean host = scanner.nextLine().toLowerCase().startsWith("h");
			if (host) {
				new Session().host();
			} else {
				new Session().join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static int port = 9923;
	
	public void host() {
		try {
			println("Starting server on port "+port);
			ServerSocket server = new ServerSocket(port);
			println("Server started");
			while(true) {
				Socket socket = server.accept();
				String sender = socket.getInetAddress().getHostAddress();
				println("Got a connection from "+sender);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				String request = dis.readUTF();
				println(sender+" sent "+request);
				if(request.equalsIgnoreCase("date")) {
					dos.writeUTF("\nServerDate: "+new Date());
					dos.flush();
				}
				try {
					dis.close();
					dos.close();
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void join() {
		try {
			Socket socket = new Socket("localhost", port);
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			DataInputStream dis = new DataInputStream(socket.getInputStream()); 
			dos.writeUTF("date");
			dos.flush();
			
			println("Server responded "+dis.readUTF());
			
			try {
				dis.close();
				dos.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			
		}
	}
	
	static void println(Object line) {
	    System.out.println(line);
	}

}
