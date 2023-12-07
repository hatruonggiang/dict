package Dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DictionaryManagement {
	private Trie trie = new Trie();

	// insert dictionary to trie
	public void setTrie(Dictionary dictionary) {
		try {
			for (Word word : dictionary) {
				trie.insert(word.getWordTarget());
			}
		} catch (NullPointerException e) {
			System.out.println("Something went wrong: " + e);
		}
	}

	// đọc dữ liệu từ file và ghi nó vào 1 dictionary dạng ArrayList<word>
	public void insertFromFile(Dictionary dictionary, String path) {
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader buf = new BufferedReader(fileReader);
			// store first value of english word
			String englishWord = buf.readLine();
			englishWord = englishWord.replace("|", "");
			String line;

			while ((line = buf.readLine()) != null) {
				Word word = new Word();
				word.setWordTarget(englishWord.trim());
				// initialize meaning
				String meaning = line + "\n";
				while ((line = buf.readLine()) != null) {
					if (!line.startsWith("|"))
						meaning += line + "\n";
					else {
						englishWord = line.replace("|", "");
						break;
					}
				}
				word.setWordExplain(meaning.trim());
				dictionary.add(word);
			}
			// close file
			buf.close();
		} catch (IOException e) {
			System.out.println("An error occur with file: " + e);
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e);
		}
	}

	// đẩy dữ liệu từ 1 dictionary vào 1 file txt
	public void exportToFile(Dictionary dictionary, String path) {
		try {
			FileWriter fileWriter = new FileWriter(path);
			BufferedWriter buf = new BufferedWriter(fileWriter);
			// write to file from current dictionary
			for (Word word : dictionary) {
				buf.write("|" + word.getWordTarget() + "\n" + word.getWordExplain());
				buf.newLine();
			}
			buf.close();
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e);
		}
	}

	// trả về 1 list các từ có khả năng đang tra nhất
	public ObservableList<String> lookupWord(Dictionary dictionary, String key) {
		ObservableList<String> list = FXCollections.observableArrayList();
		try {
			List<String> results = trie.autoComplete(key);
			if (results != null) {
				int length = Math.min(results.size(), 30);
				for (int i = 0; i < length; i++) {
					list.add(results.get(i));
				}
			}
		} catch (Exception e) {
			System.out.println("Something went wrong: " + e);
		}
		return list;
	}

	// tìm kiếm bằng binarySearch
	public int searchWord(Dictionary dictionary, String keyword) {
		try {
			dictionary.sort(new SortDictionaryByWord());
			int left = 0;
			int right = dictionary.size() - 1;
			while (left <= right) {
				int mid = left + (right - left) / 2;
				int res = dictionary.get(mid).getWordTarget().compareTo(keyword);
				if (res == 0) {
					return mid;
				}
				if (res <= 0) {
					left = mid + 1;

				} else {
					right = mid - 1;
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Null Exception.");
		}
		return -1;
	}

	// cập nhật nghĩa trong dictionary xong thì ghi nó vào file
	public void updateWord(Dictionary dictionary, int index, String meaning, String path) {
		try {
			dictionary.get(index).setWordExplain(meaning);
			exportToFile(dictionary, path);
		} catch (NullPointerException e) {
			System.out.println("Null Exception.");
		}
	}

	// xóa từ vựng, tạo trie mới,ghi lại file
	public void deleteWord(Dictionary dictionary, int index, String path) {
		try {
			dictionary.remove(index);
			trie = new Trie();
			setTrie(dictionary);
			exportToFile(dictionary, path);
		} catch (NullPointerException e) {
			System.out.println("Null Exception.");
		}
	}

	// ghi từ theo đúng định dạng: | english \n nghĩa
	public void addWord(Word word, String path) {
		try (FileWriter fileWriter = new FileWriter(path, true);
				BufferedWriter buf = new BufferedWriter(fileWriter)) {
			buf.write("|" + word.getWordTarget() + "\n" + word.getWordExplain());
			buf.newLine();
		} catch (IOException e) {
			System.out.println("IOException.");
		} catch (NullPointerException e) {
			System.out.println("Null Exception.");
		}
	}

	// async
	public void setTimeout(Runnable runnable, int delay) {
		new Thread(() -> {
			try {
				Thread.sleep(delay);
				runnable.run();
			} catch (Exception e) {
				System.err.println(e);
			}
		}).start();
	}

}