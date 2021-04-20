package com.eomcs.util;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Scanner;

// 패키지 소속 클래스 = top level class
// - 공개(public) : 다른 패키지에서 사용할 수 있음.
// - 비공개 : 같은 패키지인 경우만 사용할 수 있음.
public class Prompt {
  private BufferedReader in;
  private PrintWriter out;

  public Prompt(BufferedReader in, PrintWriter out) {
    this.in = in;
    this.out = out;
  }

  static Scanner keyboardScan = new Scanner(System.in);

  // 메서드 접근 범위 조정 
  // - public : 다른 패키지에서 사용할 수 있음.
  // - (default): 같은 패키지에 소속된 경우에만 사용할 수 있음.
  // - protected: 같은 패키지 및 자손 클래스인 경우 사용할 수 있음.
  // - private: 클래스 안에서만 사용할 수 있음.
  public String inputString(String title) throws Exception {
    out.println(title);
    out.println("!{}!");
    out.flush();
    return in.readLine();
  }

  public int inputInt(String title) throws Exception {
    return Integer.parseInt(inputString(title));
  }

  public Date inputDate(String title) throws Exception {
    return Date.valueOf(inputString(title));
  }
}
