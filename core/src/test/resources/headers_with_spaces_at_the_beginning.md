Some text

 # Header 1 {HeaderStartLeftRule}

 Setext style fully indented {HeaderStartLeftRule}
 =================================================

 Setext style title only indented {HeaderStartLeftRule}
=======================================================

* Test situations in which HeaderStartLeftRule shouldn't be triggered.

  ```rb
  # This shouldn't trigger HeaderStartLeftRule as it is a code comment.
  foo = "And here is some code"
  ```

* This is another case where HeaderStartLeftRule shouldn't be triggered
  # Test
    # Test
