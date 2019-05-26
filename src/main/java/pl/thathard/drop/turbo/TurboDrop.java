package pl.thathard.drop.turbo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurboDrop extends Turbo {

  public TurboDrop(boolean active, boolean exp, long start, long lenght) {
    super(active, exp, start, lenght);
  }
}
