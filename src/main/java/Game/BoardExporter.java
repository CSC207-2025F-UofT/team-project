package Game;
import Game.Board;
import Game.Move;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;

public class BoardExporter {
  public static ObjectMapper jsonMoveHistory(Board board){
    ObjectMapper mapper = new ObjectMapper();
    ArrayList<Move> moves = board.getMoveHistory();
    return mapper;
  }
}
