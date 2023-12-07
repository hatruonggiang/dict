package Dictionary;

import java.util.*;

public class Trie {

	protected final Map<Character, Trie> children;
	protected String content;
	protected boolean terminal = false;

	public Trie() {
		this(null);
	}

	public Trie(String content) {
		this.content = content;
		children = new HashMap<Character, Trie>();
	}

	// method to append character
	protected void add(char character) {
		String s;
		if (this.content == null) {
			s = Character.toString(character);
		} else {
			s = this.content + character;
		}
		children.put(character, new Trie(s));
	}

	// thêm chuỗi diagnosis vào cấu trúc dữ liệu Trie, duyệt qua từng kí tự của
	// diagnosis cho tới khi chưa có trong Trie thì bắt đầu thêm vào
	public void insert(String diagnosis) {
		if (diagnosis == null) {
			throw new IllegalArgumentException("Null diagnoses entries are not valid.");
		}
		Trie node = this;
		for (char c : diagnosis.toCharArray()) {
			if (!node.children.containsKey(c)) {
				node.add(c);
			}
			node = node.children.get(c);
		}
		node.terminal = true;
	}

	// tìm kiếm tất cả các từ trong Trie có tiền tố là prefix
	public List<String> autoComplete(String prefix) {
		Trie Trienode = this;
		for (char c : prefix.toCharArray()) {
			if (!Trienode.children.containsKey(c)) {
				return null;
			}
			Trienode = Trienode.children.get(c);
		}
		return Trienode.allPrefixes();
	}

	// trả về tất cả các từ có tiền tố từ node Trie hiện tại xuống dưới trong cây
	// Trie
	protected List<String> allPrefixes() {
		List<String> diagnosisresults = new ArrayList<String>();
		if (this.terminal) {
			diagnosisresults.add(this.content);
		}
		for (Map.Entry<Character, Trie> entry : children.entrySet()) {
			Trie child = entry.getValue();
			Collection<String> childPrefixes = child.allPrefixes();
			diagnosisresults.addAll(childPrefixes);
		}
		return diagnosisresults;
	}

}