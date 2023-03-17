import java.util.ArrayList;


public class PlaylistTree {

	public PlaylistNode primaryRoot;        //root of the primary B+ tree
	public PlaylistNode secondaryRoot;    //root of the secondary B+ tree

	public PlaylistTree(Integer order) {
		PlaylistNode.order = order;
		primaryRoot = new PlaylistNodePrimaryLeaf(null);
		primaryRoot.level = 0;
		secondaryRoot = new PlaylistNodeSecondaryLeaf(null);
		secondaryRoot.level = 0;
	}

	public void addSong(CengSong song) {
		primary(song);
		secondary(song);
	}

	private void primary(CengSong song) {
		int order = PlaylistNode.order;
		PlaylistNodePrimaryIndex pnIndex = new PlaylistNodePrimaryIndex(null);
		PlaylistNodePrimaryLeaf pnIndexLeaf = new PlaylistNodePrimaryLeaf(null);

		int audioId = song.audioId();
		if (primaryRoot.getType() == PlaylistNodeType.Internal) {

			pnIndex = (PlaylistNodePrimaryIndex) primaryRoot;
			while (pnIndexLeaf.getParent() == null) {
				for (int i = 0; i < pnIndex.audioIdCount(); i++) {
					if (audioId < pnIndex.audioIdAtIndex(i)) {
						if (pnIndex.getChildrenAt(i).getType() == PlaylistNodeType.Internal) {
							pnIndex = (PlaylistNodePrimaryIndex) pnIndex.getChildrenAt(i);
							break;
						} else {
							pnIndexLeaf = (PlaylistNodePrimaryLeaf) pnIndex.getChildrenAt(i);
							break;
						}
					} else if (i == pnIndex.audioIdCount() - 1) {
						if (pnIndex.getChildrenAt(i + 1).getType() == PlaylistNodeType.Internal) {
							pnIndex = (PlaylistNodePrimaryIndex) pnIndex.getChildrenAt(i + 1);
							break;
						} else {
							pnIndexLeaf = (PlaylistNodePrimaryLeaf) pnIndex.getChildrenAt(i + 1);
							break;
						}
					}
				}
			}
		}
		else if (pnIndexLeaf.getParent() == null)
		{pnIndexLeaf = (PlaylistNodePrimaryLeaf) primaryRoot;}

		if (pnIndexLeaf.songCount() == 0){
			pnIndexLeaf.addSong(0, song);
		}
		else {
			for (int i = 0; i < pnIndexLeaf.songCount(); i++) {
				if (audioId < pnIndexLeaf.audioIdAtIndex(i)) {
					pnIndexLeaf.addSong(i, song);
					break;
				} else if (i == pnIndexLeaf.songCount() - 1) {
					pnIndexLeaf.addSong(i + 1, song);
					break;
				}
			}
		}
		if (pnIndexLeaf.songCount() > 2 * order) {
			ArrayList<PlaylistNode> leaves = splitting_leaves(pnIndexLeaf);
			PlaylistNodePrimaryLeaf leaf_first = (PlaylistNodePrimaryLeaf) leaves.get(0);
			PlaylistNodePrimaryLeaf leaf_second = (PlaylistNodePrimaryLeaf) leaves.get(1);
			int middle_audioId = leaf_second.audioIdAtIndex(0);
			PlaylistNodePrimaryIndex parent = (PlaylistNodePrimaryIndex) pnIndexLeaf.getParent();
			int insert_children = 0;
			if (parent == null) {
				PlaylistNodePrimaryIndex root = new PlaylistNodePrimaryIndex(null);
				root.addAudioId(0, middle_audioId);
				leaf_first.setParent(root);
				leaf_second.setParent(root);
				root.addChildren(0, leaf_first);
				root.addChildren(1, leaf_second);
				primaryRoot = root;
			} else {
				for (int i = 0; i < parent.audioIdCount(); i++) {
					if (middle_audioId < parent.audioIdAtIndex(i)) {
						parent.addAudioId(i, middle_audioId);
						insert_children = i;
						break;
					} else if (i == parent.audioIdCount() - 1) {
						parent.addAudioId(i + 1, middle_audioId);
						insert_children = i + 1;
						break;
					}
				}
				primaryRoot = primaryRec(primaryRoot, parent, insert_children, leaf_first, leaf_second);
			}
		}

	}

	private PlaylistNode primaryRec(PlaylistNode primaryRoot, PlaylistNode pnIndex, int insert_children, PlaylistNode first_node, PlaylistNode second_node) {
		int order = PlaylistNode.order;
		PlaylistNodePrimaryIndex parent = new PlaylistNodePrimaryIndex(null);
		PlaylistNodePrimaryIndex first_int = new PlaylistNodePrimaryIndex(null);
		PlaylistNodePrimaryIndex second_int = new PlaylistNodePrimaryIndex(null);
		parent = (PlaylistNodePrimaryIndex) pnIndex;
		PlaylistNodePrimaryIndex root = new PlaylistNodePrimaryIndex(null);
		if (parent.audioIdCount() <= 2 * order)
		{
			parent.removeChildren(insert_children);
			first_node.setParent(parent);
			second_node.setParent(parent);
			parent.addChildren(insert_children, first_node);
			parent.addChildren(insert_children + 1, second_node);
			return primaryRoot;
		} else if (parent.audioIdCount() > 2 * order)
		{
			int push_up_audioId = parent.audioIdAtIndex(order);
			ArrayList<PlaylistNode> splitted_internals = split_internal(parent, insert_children, first_node, second_node);
			first_int = (PlaylistNodePrimaryIndex) splitted_internals.get(0);
			second_int = (PlaylistNodePrimaryIndex) splitted_internals.get(1);
			if (parent.getParent() == null) {

				root.addAudioId(0, push_up_audioId);
				first_int.setParent(root);
				second_int.setParent(root);
				root.addChildren(0, first_int);
				root.addChildren(1, second_int);
				return root;
			} else {
				parent = (PlaylistNodePrimaryIndex) parent.getParent();
				first_int.setParent(parent);
				second_int.setParent(parent);

				for (int i = 0; i < parent.audioIdCount(); i++) {
					if (push_up_audioId < parent.audioIdAtIndex(i)) {
						parent.addAudioId(i, push_up_audioId);
						insert_children = i;
						break;
					} else if (i == parent.audioIdCount() - 1) {
						parent.addAudioId(i + 1, push_up_audioId);
						insert_children = i + 1;
						break;
					}
				}
				root = (PlaylistNodePrimaryIndex) primaryRec(primaryRoot, parent, insert_children, first_int, second_int);
			}
		}
		return root;
	}



	private ArrayList<PlaylistNode> splitting_leaves(PlaylistNode leaf) {
		PlaylistNodePrimaryLeaf src = (PlaylistNodePrimaryLeaf) leaf;
		int order = PlaylistNode.order;
		ArrayList<CengSong> arr_first = new ArrayList<CengSong>();
		ArrayList<CengSong> arr_second = new ArrayList<CengSong>();
		for (int i = 0; i < src.songCount(); i++) {
			if (i < order)
				arr_first.add(src.songAtIndex(i));
			else
				arr_second.add(src.songAtIndex(i));
		}
		PlaylistNode leaf_first = new PlaylistNodePrimaryLeaf(null, arr_first);
		PlaylistNode leaf_second = new PlaylistNodePrimaryLeaf(null, arr_second);
		ArrayList<PlaylistNode> result = new ArrayList<PlaylistNode>();
		result.add(leaf_first);
		result.add(leaf_second);
		return result;
	}


	private ArrayList<PlaylistNode> split_internal(PlaylistNode internal, int insert_children, PlaylistNode leaf_first, PlaylistNode leaf_second) {
		PlaylistNodePrimaryIndex src = (PlaylistNodePrimaryIndex) internal;
		int order = PlaylistNode.order;
		ArrayList<Integer> arr_audioId_first = new ArrayList<Integer>();
		ArrayList<Integer> arr_audioId_second = new ArrayList<Integer>();
		ArrayList<PlaylistNode> arr_children_first = new ArrayList<PlaylistNode>();
		ArrayList<PlaylistNode> arr_children_second = new ArrayList<PlaylistNode>();
		boolean temp1 = true;
		boolean temp2 = true;
		for (int i = 0; i <= 2 * order; i++) {
			if (i < order) {
				arr_audioId_first.add(src.audioIdAtIndex(i));
			} else if (i > order) {
				arr_audioId_second.add(src.audioIdAtIndex(i));
			}
		}
		int srcInd = 0;
		for (int i = 0; i < 2 * order + 2; i++) {

			if (i <= order && i < insert_children) {
				arr_children_first.add(src.getChildrenAt(srcInd));
				srcInd++;

			} else if (i <= order && i == insert_children) {
				arr_children_first.add(leaf_first);
				temp1 = true;

			} else if (i <= order && i == insert_children + 1) {
				arr_children_first.add(leaf_second);
				srcInd++;
				temp2 = true;

			} else if (i <= order && i > insert_children + 1) {
				arr_children_first.add(src.getChildrenAt(srcInd));
				srcInd++;

			} else if (i > order && i < insert_children) {
				arr_children_second.add(src.getChildrenAt(srcInd));
				srcInd++;
			} else if (i > order && i > insert_children + 1) {
				arr_children_second.add(src.getChildrenAt(srcInd));
				srcInd++;

			} else if (i > order && i == insert_children) {
				arr_children_second.add(leaf_first);

				temp1 = false;

			} else if (i > order && i == insert_children + 1) {
				arr_children_second.add(leaf_second);
				srcInd++;
				temp2 = false;
			}
		}
		PlaylistNode first = new PlaylistNodePrimaryIndex(null, arr_audioId_first, arr_children_first);
		PlaylistNode second = new PlaylistNodePrimaryIndex(null, arr_audioId_second, arr_children_second);
		for (PlaylistNode child : ((PlaylistNodePrimaryIndex) first).getAllChildren()) {
			child.setParent(first);
		}
		for (PlaylistNode child : ((PlaylistNodePrimaryIndex) second).getAllChildren()) {
			child.setParent(second);
		}
		ArrayList<PlaylistNode> result = new ArrayList<PlaylistNode>();
		if (temp1)
			leaf_first.setParent(first);
		else
			leaf_first.setParent(second);
		if (temp2)
			leaf_second.setParent(first);
		else
			leaf_second.setParent(second);
		result.add(first);
		result.add(second);
		return result;
	}

	private void secondary(CengSong song) {
		int order = PlaylistNode.order;
		PlaylistNodeSecondaryIndex src = new PlaylistNodeSecondaryIndex(null);
		PlaylistNodeSecondaryLeaf srcLeaf = new PlaylistNodeSecondaryLeaf(null);

		String genre= song.genre();
		if (secondaryRoot.getType() == PlaylistNodeType.Internal) {

			src = (PlaylistNodeSecondaryIndex) secondaryRoot;
			while (srcLeaf.getParent() == null) {
				for (int i = 0; i < src.genreCount(); i++) {
					if (genre.compareTo(src.genreAtIndex(i))<0) {
						if (src.getChildrenAt(i).getType() == PlaylistNodeType.Internal) {
							src = (PlaylistNodeSecondaryIndex) src.getChildrenAt(i);
							break;
						}
						else if (genre.compareTo(src.genreAtIndex(i))==0) {
							if (src.getChildrenAt(i).getType() == PlaylistNodeType.Internal) {
								break;
							}
						}
						else {
							srcLeaf = (PlaylistNodeSecondaryLeaf) src.getChildrenAt(i);
							break;
						}
					}

					else if (i == src.genreCount() - 1) {
						if (src.getChildrenAt(i + 1).getType() == PlaylistNodeType.Internal) {
							src = (PlaylistNodeSecondaryIndex) src.getChildrenAt(i + 1);
							break;
						} else {
							srcLeaf = (PlaylistNodeSecondaryLeaf) src.getChildrenAt(i + 1);
							break;
						}
					}
				}
			}
		} else if (srcLeaf.getParent() == null)
			srcLeaf = (PlaylistNodeSecondaryLeaf) secondaryRoot;
		if (srcLeaf.genreCount() == 0) srcLeaf.addSong(0, song);
		else {

			for (int i = 0; i < srcLeaf.genreCount(); i++) {
				if (genre.compareTo(srcLeaf.genreAtIndex(i))<=0) {
					srcLeaf.addSong(i, song);
					break;

				}

				else if (i == srcLeaf.genreCount() - 1) {
					srcLeaf.addSong(i + 1, song);
					break;
				}
			}
		}

		if (srcLeaf.genreCount() > 2 * order) {
			ArrayList<PlaylistNode> splitted_leaves = splitting_leaves_sec(srcLeaf);
			PlaylistNodeSecondaryLeaf leaf_first = (PlaylistNodeSecondaryLeaf) splitted_leaves.get(0);
			PlaylistNodeSecondaryLeaf leaf_second = (PlaylistNodeSecondaryLeaf) splitted_leaves.get(1);
			String middle_genre = leaf_second.genreAtIndex(0);
			PlaylistNodeSecondaryIndex parent = (PlaylistNodeSecondaryIndex) srcLeaf.getParent();
			int insert_children = 0;
			if (parent == null) {
				PlaylistNodeSecondaryIndex root = new PlaylistNodeSecondaryIndex(null);
				root.addGenre(0, middle_genre);
				leaf_first.setParent(root);
				leaf_second.setParent(root);
				root.addChildren(0, leaf_first);
				root.addChildren(1, leaf_second);
				secondaryRoot = root;
			} else {
				for (int i = 0; i < parent.genreCount(); i++) {
					if (middle_genre.compareTo(parent.genreAtIndex(i)) < 0 ) {
						parent.addGenre(i, middle_genre);
						insert_children = i;
						break;
					}
					else if (middle_genre.compareTo(parent.genreAtIndex(i)) == 0 ) {
						break;
					}
					else if (i == parent.genreCount() - 1) {
						parent.addGenre(i + 1, middle_genre);
						insert_children = i + 1;
						break;
					}
				}
				PlaylistNode root = call_recursive_Secondary(secondaryRoot, parent, insert_children, leaf_first, leaf_second);
				secondaryRoot = root;
			}
		}
	}

	private PlaylistNode call_recursive_Secondary(PlaylistNode secondaryRoot, PlaylistNode src, int insert_children, PlaylistNode first_node, PlaylistNode second_node) {
		int order = PlaylistNode.order;
		PlaylistNodeSecondaryIndex parent = new PlaylistNodeSecondaryIndex(null);
		PlaylistNodeSecondaryIndex first_int = new PlaylistNodeSecondaryIndex(null);
		PlaylistNodeSecondaryIndex second_int = new PlaylistNodeSecondaryIndex(null);
		parent = (PlaylistNodeSecondaryIndex) src;
		PlaylistNodeSecondaryIndex root = new PlaylistNodeSecondaryIndex(null);
		if (parent.genreCount() <= 2 * order)
		{
			parent.removeChildren(insert_children);
			first_node.setParent(parent);
			second_node.setParent(parent);
			parent.addChildren(insert_children, first_node);
			parent.addChildren(insert_children + 1, second_node);
			return secondaryRoot;
		} else if (parent.genreCount() > 2 * order)
		{
			String push_up_genre = parent.genreAtIndex(order);
			ArrayList<PlaylistNode> splitted_internals = split_internal_sec(parent, insert_children, first_node, second_node);
			first_int = (PlaylistNodeSecondaryIndex) splitted_internals.get(0);
			second_int = (PlaylistNodeSecondaryIndex) splitted_internals.get(1);
			if (parent.getParent() == null) {

				root.addGenre(0, push_up_genre);
				first_int.setParent(root);
				second_int.setParent(root);
				root.addChildren(0, first_int);
				root.addChildren(1, second_int);
				return root;
			} else {
				parent = (PlaylistNodeSecondaryIndex) parent.getParent();
				first_int.setParent(parent);
				second_int.setParent(parent);

				for (int i = 0; i < parent.genreCount(); i++) {
					if (push_up_genre.compareTo(parent.genreAtIndex(i)) < 0) {
						parent.addGenre(i, push_up_genre);
						insert_children = i;
						break;
					}
					else if (push_up_genre.compareTo(parent.genreAtIndex(i)) == 0) {
						break;
					}
					else if (i == parent.genreCount() - 1) {
						parent.addGenre(i + 1, push_up_genre);
						insert_children = i + 1;
						break;
					}
				}
				root = (PlaylistNodeSecondaryIndex) call_recursive_Secondary(secondaryRoot, parent, insert_children, first_int, second_int);
			}
		}
		return root;
	}


	private ArrayList<PlaylistNode> splitting_leaves_sec(PlaylistNode leaf) {
		PlaylistNodeSecondaryLeaf src = (PlaylistNodeSecondaryLeaf) leaf;
		int order = PlaylistNode.order;
		ArrayList<ArrayList<CengSong>> arr_first = new ArrayList<ArrayList<CengSong>>();
		ArrayList<ArrayList<CengSong>> arr_second = new ArrayList<ArrayList<CengSong>>();
		for (int i = 0; i < src.genreCount(); i++) {
			if (i < order)
				arr_first.add(src.songsAtIndex(i));
			else
				arr_second.add(src.songsAtIndex(i));
		}
		PlaylistNode leaf_first = new PlaylistNodeSecondaryLeaf(null, arr_first);
		PlaylistNode leaf_second = new PlaylistNodeSecondaryLeaf(null, arr_second);
		ArrayList<PlaylistNode> result = new ArrayList<PlaylistNode>();
		result.add(leaf_first);
		result.add(leaf_second);
		return result;
	}


	private ArrayList<PlaylistNode> split_internal_sec(PlaylistNode internal, int insert_children, PlaylistNode leaf_first, PlaylistNode leaf_second) {
		PlaylistNodeSecondaryIndex src = (PlaylistNodeSecondaryIndex) internal;
		int order = PlaylistNode.order;
		ArrayList<String> arr_genre_first = new ArrayList<String>();
		ArrayList<String> arr_genre_second = new ArrayList<String>();
		ArrayList<PlaylistNode> arr_children_first = new ArrayList<PlaylistNode>();
		ArrayList<PlaylistNode> arr_children_second = new ArrayList<PlaylistNode>();
		boolean temp1 = true;
		boolean temp2 = true;
		for (int i = 0; i <= 2 * order; i++) {
			if (i < order) {
				arr_genre_first.add(src.genreAtIndex(i));
			} else if (i > order) {
				arr_genre_second.add(src.genreAtIndex(i));
			}
		}
		int srcInd = 0;
		for (int i = 0; i < 2 * order + 2; i++) {

			if (i <= order && i < insert_children) {
				arr_children_first.add(src.getChildrenAt(srcInd));
				srcInd++;

			} else if (i <= order && i == insert_children) {
				arr_children_first.add(leaf_first);
				temp1 = true;

			} else if (i <= order && i == insert_children + 1) {
				arr_children_first.add(leaf_second);
				srcInd++;
				temp2 = true;

			} else if (i <= order && i > insert_children + 1) {
				arr_children_first.add(src.getChildrenAt(srcInd));
				srcInd++;

			} else if (i > order && i < insert_children) {
				arr_children_second.add(src.getChildrenAt(srcInd));
				srcInd++;
			} else if (i > order && i > insert_children + 1) {
				arr_children_second.add(src.getChildrenAt(srcInd));
				srcInd++;

			} else if (i > order && i == insert_children) {
				arr_children_second.add(leaf_first);

				temp1 = false;

			} else if (i > order && i == insert_children + 1) {
				arr_children_second.add(leaf_second);
				srcInd++;
				temp2 = false;
			}
		}
		PlaylistNode first = new PlaylistNodeSecondaryIndex(null, arr_genre_first, arr_children_first);
		PlaylistNode second = new PlaylistNodeSecondaryIndex(null, arr_genre_second, arr_children_second);
		for (PlaylistNode child : ((PlaylistNodeSecondaryIndex) first).getAllChildren()) {
			child.setParent(first);
		}
		for (PlaylistNode child : ((PlaylistNodeSecondaryIndex) second).getAllChildren()) {
			child.setParent(second);
		}
		ArrayList<PlaylistNode> result = new ArrayList<PlaylistNode>();
		if (temp1)
			leaf_first.setParent(first);
		else
			leaf_first.setParent(second);
		if (temp2)
			leaf_second.setParent(first);
		else
			leaf_second.setParent(second);
		result.add(first);
		result.add(second);
		return result;
	}


	public CengSong searchSong(Integer audioId) {
		PlaylistNode searcher = primaryRoot;
		int level=0;

		while(searcher.type != PlaylistNodeType.Leaf){
			ArrayList<Integer> audioIds = ((PlaylistNodePrimaryIndex) searcher).getAudioIds();
			int idx = audioIds.size();
			for(int idx_finder = 0; idx_finder < audioIds.size(); idx_finder++){
				if( audioIds.get(idx_finder) > audioId){
					idx = idx_finder;
					break;
				}
			}
			for(int i=0;i<level;i++){System.out.print("\t");}
			System.out.println("<index>");
			for(int k: audioIds){
				for(int i=0;i<level;i++){System.out.print("\t");}
				System.out.println(k);
			}
			for(int i=0;i<level;i++){System.out.print("\t");}
			System.out.println("</index>");
			PlaylistNode child = ((PlaylistNodePrimaryIndex) searcher).getChildrenAt(idx);
			searcher = child;
		level++;
		}
		ArrayList<CengSong> songs =((PlaylistNodePrimaryLeaf) searcher).getSongs();
		CengSong holder = null;
		for(CengSong song: songs){
			if(song.audioId() == audioId){
				holder = song;
				break;
			}
		}

		if(holder == null){
			System.out.println("Could not find " + audioId);
		}
		else{
			for(int i=0;i<level;i++){System.out.print("\t");}
			System.out.println("<data>");
			for(int i=0;i<level;i++){System.out.print("\t");}
			System.out.print("<record>");
			System.out.print(holder.fullName());
			System.out.print("</record>\n");
			for(int i=0;i<level;i++){System.out.print("\t");}
			System.out.println("</data>");
		}
		return null;
	}

	public void printPrimaryPlaylist() {

		printPrimaryPlaylistRec(primaryRoot,0);

	}
	public void printPrimaryPlaylistRec(PlaylistNode node,Integer level) {

		PlaylistNode pTreeRoot = primaryRoot;
		if (node.getType() == PlaylistNodeType.Internal)
		{
			ArrayList<PlaylistNode> children = ((PlaylistNodePrimaryIndex) node).getAllChildren();


			for(int k=0;k<level;k++){System.out.print("\t");}
			System.out.println("<index>");

			for (int i = 0; i < ((PlaylistNodePrimaryIndex) node).audioIdCount() ; i++) {
				for(int k=0;k<level;k++){System.out.print("\t");}
				System.out.println(((PlaylistNodePrimaryIndex) node).audioIdAtIndex(i));

			}
			for(int k=0;k<level;k++){System.out.print("\t");}
			System.out.println("</index>");
			for(int i=0;i<children.size();i++)
				printPrimaryPlaylistRec(children.get(i),level+1);

		}

		else if (node.getType() == PlaylistNodeType.Leaf)
		{

			for(int k=0;k<level;k++){System.out.print("\t");}
			System.out.println("<data>");

			for (int i = 0; i < ((PlaylistNodePrimaryLeaf) node).songCount() ; i++)
			{
				for(int k=0;k<level;k++){System.out.print("\t");}
				System.out.print("<record>");
				System.out.print(((PlaylistNodePrimaryLeaf) node).songAtIndex(i).fullName());
				System.out.print("</record>\n");
			}
			for(int k=0;k<level;k++){System.out.print("\t");}
			System.out.println("</data>");

		}
	}
	public void printSecondaryPlaylist() {

		printSecondaryPlaylistRec(secondaryRoot,0);

	}
	public void printSecondaryPlaylistRec(PlaylistNode node,Integer level) {

		PlaylistNode pTreeRoot = secondaryRoot;

		if (node.getType() == PlaylistNodeType.Internal)
		{

			ArrayList<PlaylistNode> children = ((PlaylistNodeSecondaryIndex) node).getAllChildren();


			for(int k=0;k<level;k++){System.out.print("\t");}
			System.out.println("<index>");
			for (int i = 0; i < ((PlaylistNodeSecondaryIndex) node).genreCount() ; i++)
			{
				System.out.print(((PlaylistNodeSecondaryIndex) node).genreAtIndex(i));
				System.out.print("\n");
			}
			System.out.println("</index>");
			for(int i=0;i<children.size();i++)
				printSecondaryPlaylistRec(children.get(i),level+1);
		}

		else if (node.getType() == PlaylistNodeType.Leaf)
		{

			for(int k=0;k<level;k++){System.out.print("\t");}
			System.out.println("<data>");

			for (int i = 0; i < ((PlaylistNodeSecondaryLeaf) node).genreCount() ; i++)
			{
				for(int k=0;k<level;k++){System.out.print("\t");}
				System.out.println(((PlaylistNodeSecondaryLeaf) node).genreAtIndex(i));
				for(int j=0;j<((PlaylistNodeSecondaryLeaf) node).songsAtIndex(i).size();j++) {
					for(int k=0;k<level+1;k++){System.out.print("\t");}
					System.out.print("<record>");
					System.out.print(((PlaylistNodeSecondaryLeaf) node).songsAtIndex(i).get(j).fullName());
					System.out.print("</record>\n");
				}
			}
			for(int k=0;k<level;k++){System.out.print("\t");}
			System.out.println("</data>");
		}

	}


}


