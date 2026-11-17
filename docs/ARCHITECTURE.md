# Minimal Shared Scaffold

The package structure follows the course
[CA Login example](https://github.com/CSC207-2026F-UofT/ca-login/blob/main/LAB_INSTRUCTIONS.md).
Only application startup, the opening Home layout, and the existing offline data
reader are implemented.

## Feature packages

`use_case`, `interface_adapter`, and `view` each contain four feature folders:
`search`, `customize`, `profile`, and `browse`.

Each use case group contains empty InputBoundary, InputData, Interactor,
OutputBoundary, OutputData, and DataAccessInterface declarations. Each adapter
group contains empty Controller, Presenter, ViewModel, and State declarations.
Owners define their contracts and implementation; there are no feature methods,
fields, inheritance relationships, or TODO flows in these starting files.

The opening Home layout uses `view/browse/MainPageView` and
`view/search/SearchView`. Other View folders are tracked with `.gitkeep`.
No Details, Login, Register, Profile, or Customize pages or navigation are provided.

## Working shared code

- Main schedules window construction on the Swing EDT.
- AppBuilder creates the JFrame and Home layout.
- Home displays a title, search controls, an inactive Profile button, and a catalog
  placeholder. All feature buttons are disabled and have no action listeners.
- The existing dataset, index, Entities, Reader, DataReadCheck, and Reader tests
  are unchanged. DataReadCheck loads the dataset; GUI startup does not.

## Extending the scaffold

Owners implement their own input/output contracts, business rules, adapters,
Views, concrete data access, and tests. AppBuilder connects completed components.
Coordinate shared Entity and Home layout changes. Keep Swing rendering in Views,
business rules in use cases, and concrete storage/HTTP code in data_access.
Navigation, API integration, and user-data persistence remain future owner work.
