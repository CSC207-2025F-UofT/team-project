package use_case_layer.flashcard_generator;

import java.util.List;
//import java.util.SequencedCollection;

public class FlashcardGeneratorInputData {
    private List<TextPiece> listTextPiece;

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

            public String getText(){
                return this.text;
            }
            public boolean isBold(){
                return this.isBold;
            }



        }
        public List<TextPiece> getListTextPiece(){ return this.listTextPiece;}


    }
