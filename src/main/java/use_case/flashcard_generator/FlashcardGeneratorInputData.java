package use_case.flashcard_generator;

import java.util.List;
//import java.util.SequencedCollection;

/**
 * Stores TextPieces provided as InputData for flashcard generation use case.
 *
 */
public class FlashcardGeneratorInputData {
    private final List<TextPiece> listTextPiece;

    public FlashcardGeneratorInputData(List<TextPiece> listTextPiece) {
        this.listTextPiece = listTextPiece;
    }

        public static class TextPiece {
            private final String text;
            private final boolean isBold;

            public TextPiece(String text, boolean isBold){
                this.text = text;
                this.isBold = isBold;

            }

            /**
             * An object that represents a piece of text and whether it is bolded
             * @return
             */
            public String getText(){
                return this.text;
            }
            public boolean isBold(){
                return this.isBold;
            }



        }
        public List<TextPiece> getListTextPiece(){ return this.listTextPiece;}

    /**
     * Returns TextPiece at given index
     * @param index the index we want to retrieve
     * @return the TextPiece at index
     */
    public TextPiece getTextPiece(int index){
        return this.listTextPiece.get(index);
    }


    }
