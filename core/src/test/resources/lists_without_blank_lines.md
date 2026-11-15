* list (on first line)

text

* list

text
* list {BlanksAroundListsRule}
text
+ list {BlanksAroundListsRule}
text
- list {BlanksAroundListsRule}
text
1. list {BlanksAroundListsRule}
text

* list
* list {BlanksAroundListsRule}
text

text
10. list {BlanksAroundListsRule}
20. list

text

* list
  * list
    * list

text

* list
  with hanging indent
  * list
    with hanging indent
* list
  with hanging indent

Note: list without hanging indent violates BlanksAroundListsRule

* list

  item with blank lines

* list

  item with blank lines

text

```js
/*
 * code block
 * not a list
 */
```

text

* list {BlanksAroundListsRule}
``` {BlanksAroundFencesRule}
code
```

text

```js
code
``` {BlanksAroundFencesRule}
* list {BlanksAroundListsRule}

text

* list (on last line without newline)