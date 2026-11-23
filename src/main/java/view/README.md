# UC2 Swing page in the shared view folder

**Responsible member for UC2:** Xinjia Shen.

`ClassChallengeView` is the Lecture Hall screen. It shows assigned courses, confirmation, questions,
feedback and results. It delegates actions to the controller, runs background work with SwingWorker,
and renders updates on the Swing event dispatch thread.

The caller supplies `backToStreet`. Navigation away from an active challenge should use `requestLeave()`
so cancellation is confirmed before returning to Street.

`GameView` is shared navigation. Its four-argument constructor accepts the actual Lecture Hall panel;
the original constructor remains available. The default application still uses a placeholder until
the real shared-state adapter is connected.

Street/building navigation is maintained with UC3. Title/Continue and Residence screens support UC4/UC5.
This folder is shared; UC2 does not own every screen in it.
