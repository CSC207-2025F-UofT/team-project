package entity;

import entity.state.ObjectState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionModelTest {
    @Test
    void descriptionsFollowConfiguredModeAndSnapshotsAreDetached() {
        Interactable drawer = drawer(action(false, Optional.empty(), List.of(), List.of()));
        ObjectState before = drawer.snapshot();
        assertEquals("A closed drawer.", drawer.describe());

        drawer.setStateValue("mode", "open");
        assertEquals("An open drawer.", drawer.describe());
        assertEquals("closed", before.values().get("mode"));
        assertEquals("open", drawer.snapshot().values().get("mode"));
        assertNotEquals(before, drawer.snapshot());
    }

    @Test
    void nestedConfigurationAndRuntimeQueriesCannotBeMutatedByCallers() {
        Action open = action(false, Optional.empty(), List.of(), List.of());
        Map<String, String> descriptions = new HashMap<>(Map.of("closed", "Closed", "open", "Open"));
        Map<String, String> initial = new HashMap<>(Map.of("mode", "closed"));
        Set<String> modes = new LinkedHashSet<>(Set.of("closed", "open"));
        Map<String, Set<String>> allowed = new HashMap<>(Map.of("mode", modes));
        Map<String, Action> actions = new HashMap<>(Map.of("open", open));
        Interactable drawer = new Interactable("drawer", "Drawer", descriptions, initial, allowed, actions);

        descriptions.clear();
        initial.put("mode", "open");
        modes.clear();
        allowed.clear();
        actions.clear();

        assertEquals("Closed", drawer.describe());
        assertTrue(drawer.acceptsStateValue("mode", "open"));
        assertEquals(List.of(open), drawer.actions());
        assertThrows(UnsupportedOperationException.class, () -> drawer.actions().clear());
        assertThrows(UnsupportedOperationException.class, () -> drawer.snapshot().values().clear());
        assertThrows(UnsupportedOperationException.class, () -> drawer.snapshot().completedActionIds().clear());
    }

    @Test
    void constructorsRejectMissingModesInvalidStateAndMismatchedActions() {
        Action open = action(false, Optional.empty(), List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> new Interactable("drawer", "Drawer",
                Map.of("closed", "Closed"), Map.of("mode", "closed"),
                Map.of("mode", Set.of("closed", "open")), Map.of("open", open)));
        assertThrows(IllegalArgumentException.class, () -> new Interactable("drawer", "Drawer",
                Map.of("closed", "Closed"), Map.of("mode", "unknown"),
                Map.of("mode", Set.of("closed")), Map.of("open", open)));
        assertThrows(IllegalArgumentException.class, () -> new Interactable("drawer", "Drawer",
                Map.of("closed", "Closed"), Map.of("mode", "closed"),
                Map.of("mode", Set.of("closed")), Map.of("wrong-key", open)));
        assertThrows(IllegalArgumentException.class, () -> new Interactable("drawer", "Drawer",
                Map.of("closed", "Closed"), Map.of("other", "closed"),
                Map.of("other", Set.of("closed")), Map.of()));
    }

    @Test
    void objectStateRejectsUnknownKeysAndInvalidValuesWithoutChangingAnything() {
        Interactable drawer = drawer(action(false, Optional.empty(), List.of(), List.of()));
        ObjectState before = drawer.snapshot();
        assertEquals(ResultCode.INVALID_TARGET,
                assertThrows(DomainException.class, () -> drawer.stateValue("missing")).code());
        assertEquals(ResultCode.INVALID_EFFECT,
                assertThrows(DomainException.class, () -> drawer.setStateValue("mode", "broken")).code());
        assertFalse(drawer.acceptsStateValue("missing", "open"));
        assertEquals(before, drawer.snapshot());
    }

    @Test
    void availabilityChecksEveryConditionAndEffectWithoutMutatingState() {
        Facts facts = new Facts();
        Action open = action(false, Optional.empty(),
                List.of(new AllOfCondition(List.of(
                        new ObjectStateCondition("drawer", "mode", "closed"),
                        new ObjectStateCondition("switch", "mode", "on")))),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));

        assertTrue(open.isAvailable(facts, Set.of()));
        assertEquals("closed", facts.objectState("drawer", "mode"));
        facts.values.put("switch", "off");
        assertFalse(open.isAvailable(facts, Set.of()));
        assertEquals(ResultCode.UNMET_CONDITION,
                assertThrows(DomainException.class, () -> open.prepare("drawer", facts, Optional.empty())).code());
        assertEquals("closed", facts.objectState("drawer", "mode"));
    }

    @Test
    void missingConditionTargetsAreUnavailableAndDoNotBecomeEffects() {
        Action open = action(false, Optional.empty(),
                List.of(new ObjectStateCondition("absent", "mode", "on")), List.of());
        assertFalse(open.isAvailable(new Facts(), Set.of()));
        assertEquals(ResultCode.INVALID_TARGET,
                assertThrows(DomainException.class,
                        () -> open.prepare("drawer", new Facts(), Optional.empty())).code());
    }

    @Test
    void toolMustMatchTheExactConfiguredIdAndIsNotChangedByPreparation() {
        Action open = action(false, Optional.of("brass-key"), List.of(), List.of());
        Collectable key = new Collectable("brass-key", "Key", "A brass key.", true);
        Collectable wrong = new Collectable("other-key", "Key", "Another key.", true);
        Facts facts = new Facts();

        assertFalse(open.isAvailable(facts, Set.of("other-key")));
        assertTrue(open.isAvailable(facts, Set.of("brass-key")));
        assertEquals(ResultCode.MISSING_ITEM,
                assertThrows(DomainException.class, () -> open.prepare("drawer", facts, Optional.empty())).code());
        assertEquals(ResultCode.WRONG_ITEM,
                assertThrows(DomainException.class, () -> open.prepare("drawer", facts, Optional.of(wrong))).code());
        assertEquals("open", open.prepare("drawer", facts, Optional.of(key)).actionId());
        assertTrue(key.isRevealed());
    }

    @Test
    void actionsWithoutToolRequirementsRejectExtraneousSelectedTools() {
        Action open = action(false, Optional.empty(), List.of(), List.of());
        assertEquals(ResultCode.WRONG_ITEM, assertThrows(DomainException.class,
                () -> open.prepare("drawer", new Facts(), Optional.of(
                        new Collectable("key", "Key", "A key.", true)))).code());
    }

    @Test
    void invalidEffectIsRejectedBeforeAnyAssignment() {
        Facts facts = new Facts();
        Action open = action(false, Optional.empty(), List.of(), List.of(
                new SetObjectStateEffect("drawer", "mode", "open"),
                new SetObjectStateEffect("absent", "mode", "open")));
        assertFalse(open.isAvailable(facts, Set.of()));
        assertEquals(ResultCode.INVALID_EFFECT,
                assertThrows(DomainException.class, () -> open.prepare("drawer", facts, Optional.empty())).code());
        assertEquals("closed", facts.objectState("drawer", "mode"));
    }

    @Test
    void planConfigurationIsDefensivelyCopiedAndCannotBeChanged() {
        Set<String> appearances = new LinkedHashSet<>(Set.of("card"));
        List<Effect> effects = new ArrayList<>(List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        List<Condition> conditions = new ArrayList<>(List.of(new ObjectStateCondition("drawer", "mode", "closed")));
        Action open = new Action("open", "Open", Optional.empty(), appearances, "Opened.", false,
                conditions, effects);
        Interactable drawer = drawer(open);
        ActionPlan plan = drawer.prepare("open", Optional.empty(), new Facts());
        appearances.clear();
        effects.clear();
        conditions.clear();

        assertEquals(Set.of("card"), plan.appearingItemIds());
        assertEquals(1, plan.effects().size());
        assertEquals("Opened.", plan.resultText());
        assertThrows(UnsupportedOperationException.class, () -> plan.effects().clear());
        assertThrows(UnsupportedOperationException.class, () -> plan.appearingItemIds().clear());
    }

    @Test
    void plansAreBoundToTheExactRuntimeOwnerEvenWhenActionsAreShared() {
        Action shared = action(false, Optional.empty(), List.of(), List.of());
        Interactable original = drawer(shared);
        Interactable otherSession = drawer(shared);
        ActionPlan plan = original.prepare("open", Optional.empty(), new Facts());

        assertTrue(plan.matches(original, shared));
        assertFalse(plan.matches(otherSession, shared));
        assertFalse(plan.matches(original, action(false, Optional.empty(), List.of(), List.of())));
        assertFalse(shared.prepare("drawer", new Facts(), Optional.empty()).matches(original, shared));
        assertThrows(DomainException.class, () -> plan.forOwner(otherSession));
    }

    @Test
    void stalePlanRechecksItsOriginalConditionsBeforeCommit() {
        Action open = action(false, Optional.empty(),
                List.of(new ObjectStateCondition("drawer", "mode", "closed")), List.of());
        Facts facts = new Facts();
        ActionPlan plan = drawer(open).prepare("open", Optional.empty(), facts);
        facts.values.put("drawer", "open");
        assertEquals(ResultCode.UNMET_CONDITION,
                assertThrows(DomainException.class, () -> plan.validate(facts)).code());
    }

    @Test
    void onlyNonrepeatableActionsRecordCompletionAndCompletionPrecedesToolValidation() {
        Interactable oneShot = drawer(action(false, Optional.of("key"), List.of(), List.of()));
        assertTrue(oneShot.markCompleted("open"));
        assertFalse(oneShot.markCompleted("open"));
        assertEquals(Set.of("open"), oneShot.snapshot().completedActionIds());
        assertEquals(ResultCode.ACTION_COMPLETED,
                assertThrows(DomainException.class,
                        () -> oneShot.prepare("open", Optional.empty(), new Facts())).code());

        Interactable repeatable = drawer(action(true, Optional.empty(), List.of(), List.of()));
        assertFalse(repeatable.markCompleted("open"));
        assertFalse(repeatable.isActionCompleted("open"));
        assertTrue(repeatable.snapshot().completedActionIds().isEmpty());
        repeatable.prepare("open", Optional.empty(), new Facts());
    }

    @Test
    void unsupportedEffectsAndEmptyConjunctionsAreRejected() {
        Effect external = new Effect() {
            @Override public void validate(RoomReadState facts) { }
            @Override public void apply(RoomMutation target) { throw new IllegalStateException("external failure"); }
        };
        assertThrows(IllegalArgumentException.class,
                () -> action(false, Optional.empty(), List.of(), List.of(external)));
        assertThrows(IllegalArgumentException.class, () -> new AllOfCondition(List.of()));
    }

    @Test
    void newPureConditionsWorkDirectlyAndInsideConjunctionsWithoutChangingOrchestration() {
        Condition custom = new StateValuePrefixCondition("drawer", "mode", "cl");
        for (Condition configured : List.of(custom, new AllOfCondition(List.of(custom)))) {
            Action open = new Action("open", "Open drawer", Optional.empty(), Set.of(),
                    "The drawer opens.", true, List.of(configured),
                    List.of(new SetObjectStateEffect("drawer", "mode", "open")));
            Interactable drawer = drawer(open);
            Room room = room(drawer);
            ObjectState before = drawer.snapshot();

            assertEquals(1, room.availableActions(Set.of()).size());
            ActionPlan plan = room.prepareInteraction("drawer", "open", Optional.empty());
            assertEquals(before, drawer.snapshot(), "Condition evaluation must not mutate state");
            assertTrue(room.applyInteraction(plan).changed());
            assertEquals("open", drawer.snapshot().values().get("mode"));

            ObjectState after = drawer.snapshot();
            assertTrue(room.availableActions(Set.of()).isEmpty());
            assertEquals(ResultCode.UNMET_CONDITION,
                    assertThrows(DomainException.class,
                            () -> room.prepareInteraction("drawer", "open", Optional.empty())).code());
            assertEquals(after, drawer.snapshot(), "An unmet extension condition must preserve state");
            assertTrue(after.completedActionIds().isEmpty(), "Repeatable actions keep no completion record");
        }
    }

    @Test
    void objectStateCopiesItsInputsAndUsesValueEquality() {
        Map<String, String> values = new LinkedHashMap<>(Map.of("mode", "closed"));
        Set<String> completed = new LinkedHashSet<>(Set.of("inspect"));
        ObjectState state = new ObjectState("drawer", values, completed);
        values.clear();
        completed.clear();
        ObjectState equal = new ObjectState("drawer", Map.of("mode", "closed"), Set.of("inspect"));
        assertEquals(equal, state);
        assertEquals(equal.hashCode(), state.hashCode());
    }

    @Test
    void roomRejectsAPlanFromAnotherSessionBeforeChangingTheTargetWorld() {
        Action shared = action(false, Optional.empty(), List.of(),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Interactable original = drawer(shared);
        Interactable otherSession = drawer(shared);
        Room source = room(original);
        Room target = room(otherSession);
        ActionPlan plan = source.prepareInteraction("drawer", "open", Optional.empty());
        ObjectState before = otherSession.snapshot();

        assertEquals(ResultCode.INVALID_INPUT,
                assertThrows(DomainException.class, () -> target.applyInteraction(plan)).code());
        assertEquals(before, otherSession.snapshot());
        assertEquals("A closed drawer.", original.describe());
    }

    @Test
    void roomRejectsAStaleConditionPlanBeforeApplyingAnyEffectsOrCompletion() {
        Action open = action(false, Optional.empty(),
                List.of(new ObjectStateCondition("drawer", "mode", "closed")),
                List.of(new SetObjectStateEffect("drawer", "mode", "open")));
        Interactable drawer = drawer(open);
        Room room = room(drawer);
        ActionPlan plan = room.prepareInteraction("drawer", "open", Optional.empty());
        drawer.setStateValue("mode", "open");
        ObjectState before = drawer.snapshot();

        assertEquals(ResultCode.UNMET_CONDITION,
                assertThrows(DomainException.class, () -> room.applyInteraction(plan)).code());
        assertEquals(before, drawer.snapshot());
        assertFalse(drawer.isActionCompleted("open"));
    }

    @Test
    void changedReflectsTheFinalWorldStateRatherThanIntermediateAssignments() {
        Action netZero = action(true, Optional.empty(), List.of(), List.of(
                new SetObjectStateEffect("drawer", "mode", "open"),
                new SetObjectStateEffect("drawer", "mode", "closed")));
        Interactable drawer = drawer(netZero);
        Room room = room(drawer);

        DomainResult result = room.applyInteraction(room.prepareInteraction("drawer", "open", Optional.empty()));
        assertFalse(result.changed());
        assertEquals(ResultCode.NO_CHANGE, result.code());
        assertEquals("A closed drawer.", drawer.describe());
        assertTrue(drawer.snapshot().completedActionIds().isEmpty());
    }

    @Test
    void completingAOneShotActionCountsAsAChangeEvenWithoutStateEffects() {
        Interactable drawer = drawer(action(false, Optional.empty(), List.of(), List.of()));
        Room room = room(drawer);
        ActionPlan plan = room.prepareInteraction("drawer", "open", Optional.empty());

        DomainResult result = room.applyInteraction(plan);
        assertTrue(result.changed());
        assertEquals(Set.of("open"), drawer.snapshot().completedActionIds());
        ObjectState completed = drawer.snapshot();
        assertEquals(ResultCode.ACTION_COMPLETED,
                assertThrows(DomainException.class, () -> room.applyInteraction(plan)).code());
        assertEquals(completed, drawer.snapshot());
    }

    private static Room room(Interactable drawer) {
        return new Room("room", "Room", "A room.",
                new TriviaLock("trivia", "Question", "Correct.", Optional.empty()),
                new ItemLock("door", "Door", "Opened.", Set.of("card")),
                Optional.empty(), Map.of("drawer", drawer), Map.of());
    }

    private static Action action(boolean repeatable, Optional<String> tool,
                                 List<Condition> conditions, List<Effect> effects) {
        return new Action("open", "Open drawer", tool, Set.of("card"), "The drawer opens.", repeatable,
                conditions, effects);
    }

    private static Interactable drawer(Action open) {
        return new Interactable("drawer", "Drawer", Map.of("closed", "A closed drawer.", "open", "An open drawer."),
                Map.of("mode", "closed"), Map.of("mode", Set.of("closed", "open")), Map.of("open", open));
    }

    /** An immutable extension used without changing Action, Room, or AllOfCondition. */
    private static final class StateValuePrefixCondition implements Condition {
        private final String objectId;
        private final String key;
        private final String prefix;

        private StateValuePrefixCondition(String objectId, String key, String prefix) {
            this.objectId = objectId;
            this.key = key;
            this.prefix = prefix;
        }

        @Override
        public boolean test(RoomReadState facts) {
            return facts.objectState(objectId, key).startsWith(prefix);
        }
    }

    private static final class Facts implements RoomReadState {
        private final Map<String, String> values = new HashMap<>(Map.of("drawer", "closed", "switch", "on"));

        @Override
        public String objectState(String objectId, String key) {
            if (!values.containsKey(objectId) || !key.equals("mode")) {
                throw new DomainException(ResultCode.INVALID_TARGET, "Missing object state");
            }
            return values.get(objectId);
        }

        @Override
        public boolean acceptsObjectState(String objectId, String key, String value) {
            return values.containsKey(objectId) && key.equals("mode")
                    && (objectId.equals("drawer") ? Set.of("open", "closed") : Set.of("on", "off")).contains(value);
        }
    }
}
