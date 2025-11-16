// package use_case.upvoting_downvoting

// upvote() and downvote() for Post class
//public void upvote(Post post){
//    post.votes[0] += 1;
//}
//
//public void downvote(Post post){
//    post.votes[1] += 1;
//}

//import java.util.List;
//import java.util.Collections;
//import java.util.ArrayList;
//import java.util.Arrays; // Needed for array manipulation

//    /**
//     * Reorders the replies list using Merge Sort based on the vote score (descending).
//     */
//    public void reorderReplies() {
//        if (this.replies == null || this.replies.size() <= 1) {
//            return;
//        }
//
//        // 1. Convert the List to an array for efficient sorting
//        ReplyPost[] sortedReplies = mergeSort(this.replies.toArray(new ReplyPost[0]));
//
//        // 2. Update the instance variable list with the sorted results
//        this.replies = new ArrayList<>(Arrays.asList(sortedReplies));
//    }
//
//    /**
//     * Recursive Merge Sort implementation for ReplyPost arrays.
//     * @param array The array of ReplyPost objects to sort.
//     * @return The sorted ReplyPost array.
//     */
//    private ReplyPost[] mergeSort(ReplyPost[] array) {
//        int n = array.length;
//        if (n <= 1) {
//            return array;
//        }
//
//        int mid = n / 2;
//
//        // Create left and right subarrays
//        ReplyPost[] left = new ReplyPost[mid];
//        System.arraycopy(array, 0, left, 0, mid);
//
//        ReplyPost[] right = new ReplyPost[n - mid];
//        System.arraycopy(array, mid, right, 0, n - mid);
//
//        // Recursively sort the two halves
//        left = mergeSort(left);
//        right = mergeSort(right);
//
//        // Merge the results
//        return merge(left, right);
//    }
//
//    /**
//     * Merges two sorted arrays of ReplyPost objects in descending score order.
//     * @param left The sorted left subarray.
//     * @param right The sorted right subarray.
//     * @return The merged and sorted array.
//     */
//    private ReplyPost[] merge(ReplyPost[] left, ReplyPost[] right) {
//        ReplyPost[] result = new ReplyPost[left.length + right.length];
//        int i = 0, j = 0, k = 0; // i for left, j for right, k for result
//
//        while (i < left.length && j < right.length) {
//            // Comparison for DESCENDING order (highest score first):
//            // If left[i] has a score greater than or equal to right[j]'s score,
//            // it comes first.
//            if (left[i].getScore() >= right[j].getScore()) {
//                result[k++] = left[i++];
//            } else {
//                result[k++] = right[j++];
//            }
//        }
//
//        // Copy remaining elements from left array
//        while (i < left.length) {
//            result[k++] = left[i++];
//        }
//
//        // Copy remaining elements from right array
//        while (j < right.length) {
//            result[k++] = right[j++];
//        }
//
//        return result;
//    }
//
//    public void addReply(ReplyPost reply) {
//        this.replies.add(reply);
//        // Automatically reorder after adding a new reply (optional)
//        reorderReplies();
//    }


