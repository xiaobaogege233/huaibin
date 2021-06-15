package com.dml.users;

/**
 * 发布方给出的唯一字符串作为key
 * 
 * @author neo
 *
 */
public class PublisherUniqueStringAuthKey implements AuthKey {

	private String publisher;

	private String uuid;

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public boolean equals(Object o) {
		if (o instanceof PublisherUniqueStringAuthKey) {
			PublisherUniqueStringAuthKey pusak = (PublisherUniqueStringAuthKey) o;
			return pusak.publisher.equals(publisher) && pusak.uuid.equals(uuid);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return publisher.hashCode() * 10 + uuid.hashCode();
	}

}
