/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Christian W. Damus (CEA) - initial API and implementation
 */
package org.eclipse.oomph.internal.setup.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * An utility for expansion of potentially nested variable expressions, such as
 * <tt>${some.${nested|filter}.expression|filter}</tt>.
 */
public class StringExpander
{

  private final SetupTaskPerformer context;

  public StringExpander(SetupTaskPerformer context)
  {
    this.context = context;
  }

  public String expandString(String string, Set<String> keys, boolean secure)
  {
    List<Token> tokens = tokenize(string);

    StringBuilder result = new StringBuilder();
    for (Token next : tokens)
    {
      String body = next.text();

      switch (next.type)
      {
        case Token.EXPANSION:
          // Recursively expand nested expressions
          String expanded = expandString(body, keys, secure);
          while (expanded != null && !expanded.equals(body))
          {
            // Some substitutions happened. Repeat
            body = expanded;
            expanded = expandString(body, keys, secure);
          }

          // Final leaf expansion of a non-nested expression
          String expression = "${" + body + "}"; //$NON-NLS-1$ //$NON-NLS-2$
          expanded = context.doExpandString(expression, keys, secure);
          if (expanded != null)
          {
            result.append(expanded);
          }
          else
          {
            result.append(expression); // Unresolved
          }
          break;
        default:
          result.append(body);
          break;
      }
    }

    return result.toString();
  }

  private List<Token> tokenize(String string)
  {
    List<Token> result = new ArrayList<Token>(3); // don't anticipate many tokens
    final int length = string.length();
    int start = 0;

    for (int i = 0; i < length; i++)
    {
      char ch = string.charAt(i);
      if (ch == '$')
      {
        // Token boundary
        if (i > start)
        {
          // Carve off a token
          result.add(new Token(Token.TEXT, string, start, i));
        }

        if (i == length - 1)
        {
          // Dangling '$' and we're done
          result.add(new Token(Token.TEXT, string, i, length));
        }
        else
        {
          char peek = string.charAt(i + 1);
          switch (peek)
          {
            case '$':
              // Escaped '$'. Skip the first '$' and take a token
              i++;
              start = i + 1;
              result.add(new Token(Token.TEXT, string, i, start));
              break;
            case '{':
              // Expansion. Scan to the matching '}'
              int nesting = 1;
              scan: for (int j = i + 2; j < length; j++)
              {
                char next = string.charAt(j);
                switch (next)
                {
                  case '$':
                    // Look for nesting
                    if (j + 1 < length)
                    {
                      char ahead = string.charAt(j + 1);
                      if (ahead == '{')
                      {
                        nesting++;
                      }
                    }
                    break;
                  case '}':
                    if (--nesting <= 0)
                    {
                      // Found the close
                      result.add(new Token(Token.EXPANSION, string, i + 2, j));
                      start = j + 1;
                      i = j;
                      break scan;
                    }
                }
              }
              break;
            default:
              // Dangling '$'
              start = i + 1;
              result.add(new Token(Token.TEXT, string, i, start));
              break;
          }
        }
      }
    }

    if (start < length)
    {
      // Add the last token
      result.add(new Token(Token.TEXT, string, start, length));
    }

    return result;
  }

  private static class Token
  {
    static final int TEXT = 0;

    static final int EXPANSION = 1;

    final int type;

    final String string;

    final int start;

    final int end;

    Token(int type, String string, int start, int end)
    {
      this.type = type;
      this.string = string;
      this.start = start;
      this.end = end;
    }

    String text()
    {
      return string.substring(start, end);
    }

    @Override
    public String toString()
    {
      return String.format("Token(%s)", text());
    }
  }
}
