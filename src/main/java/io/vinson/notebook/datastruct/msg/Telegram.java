
package io.vinson.notebook.datastruct.msg;


/**
 * 电报消息
 * @author: jiangweixin
 * @date: 2019/5/15
 */
public class Telegram implements Comparable<Telegram> {

	public static final int RETURN_RECEIPT_UNNEEDED = 0;

	public static final int RETURN_RECEIPT_NEEDED = 1;

	public static final int RETURN_RECEIPT_SENT = 2;

	public Telegraph sender;

	public Telegraph receiver;

	public int message;

	public int returnReceiptStatus;

	private long timestamp;

	public Object extraInfo;

	public Telegram() {
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void reset () {
		this.sender = null;
		this.receiver = null;
		this.message = 0;
		this.returnReceiptStatus = RETURN_RECEIPT_UNNEEDED;
		this.extraInfo = null;
		this.timestamp = 0;
	}

	@Override
	public int compareTo(Telegram other) {
		if (this.equals(other)) return 0;
		return (this.timestamp - other.timestamp < 0) ? -1 : 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + message;
		result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + Float.floatToIntBits(timestamp);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Telegram other = (Telegram)obj;
		if (message != other.message) return false;
		if (Float.floatToIntBits(timestamp) != Float.floatToIntBits(other.timestamp)) return false;
		if (sender == null) {
			if (other.sender != null) return false;
		} else if (!sender.equals(other.sender)) return false;
		if (receiver == null) {
			if (other.receiver != null) return false;
		} else if (!receiver.equals(other.receiver)) return false;
		return true;
	}

}
