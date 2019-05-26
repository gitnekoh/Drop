package pl.thathard.drop.turbo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurboDropGlobal extends Turbo {

  public TurboDropGlobal(boolean active, boolean exp, long start, long lenght) {
    super(active, exp, start, lenght);
  }
}
