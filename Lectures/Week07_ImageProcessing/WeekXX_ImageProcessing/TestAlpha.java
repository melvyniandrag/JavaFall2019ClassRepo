import java.nio.ByteBuffer;

public class TestAlpha {
	public static void main(String[] args){
		int FF = 0xFF;
		int alpha = FF << 24;
		System.out.println(String.valueOf(alpha));
		byte[] bytes = ByteBuffer.allocate(4).putInt(alpha).array();
		for( byte b : bytes){
			System.out.println(String.format("%02X", b));
		}
		System.out.println(String.valueOf(0xFF000000));
	}
}

