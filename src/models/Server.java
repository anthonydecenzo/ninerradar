package models;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Server {
	
	private InetSocketAddress serverAddress;
	private int serverPort;
	private AsynchronousServerSocketChannel server;
	private Future<AsynchronousSocketChannel> acceptConnection;
	private AsynchronousSocketChannel currentClient;
	private ByteBuffer buffer;
	
	public Server(int serverPort) {
		this.serverPort = serverPort;
		init();
	}
	
	private void init() {
		setServerAddress(new InetSocketAddress("127.0.0.1", serverPort));
		try {
			server = AsynchronousServerSocketChannel.open();
			acceptConnection = server.accept();
			currentClient = acceptConnection.get(10, TimeUnit.SECONDS);
			buffer = ByteBuffer.allocate(1024);
		} catch (IOException | InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String receiveClientMessage() {
		StringBuilder message = new StringBuilder("null");
		
		if(currentClient != null && currentClient.isOpen()) {
			
			
			Future<Integer> value = currentClient.read(buffer);
			message.setLength(0);
			message.append(new String(buffer.array()).trim());

			try {
				value.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			buffer.flip();
		}
		
		return message.toString();
	}
	
	public void writeCommand(String command) {
		Future<Integer> writeValue = currentClient.write(ByteBuffer.wrap(command.getBytes()));
		
		try {
			writeValue.get();
			buffer.clear();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public InetSocketAddress getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}	
	
}
