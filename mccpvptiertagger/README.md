# MC Cpvp TL Tier Tagger

A Fabric client mod for Minecraft 1.21.11 that shows a player's Crystal PvP
tier next to their nametag, pulled from your own tier list (not MCTiers/PVPTiers).

You will NOT need to install Java, Gradle, or any dev tools on your own
computer. GitHub builds the mod for you for free. You just need two GitHub
repos and to edit one text file whenever you tier-test someone.

---

## Part 1 — Create your tier list repo (stores who has what tier)

1. Go to github.com and create a free account if you don't have one.
2. Click **+ → New repository**. Name it something like `mccpvptl-tiers`. Set it to **Public**. Create it.
3. Click **Add file → Create new file**, name it `tiers.json`, and paste:
   ```json
   {
     "Steve": "HT1",
     "Alex": "LT3"
   }
   ```
4. Click **Commit changes**.
5. Click on `tiers.json` in your repo, then click the **Raw** button. Copy that URL —
   it'll look like:
   `https://raw.githubusercontent.com/YOUR-USERNAME/mccpvptl-tiers/main/tiers.json`

**This is the only file you'll ever touch after setup.** Every time you
tier-test someone, come back here, click the pencil (Edit), add a line, Commit.

---

## Part 2 — Point the mod at your tier list repo

1. Open `src/main/java/com/mccpvptl/tiertagger/TierManager.java` in this project (right in GitHub's web editor is fine — click the pencil icon).
2. Find this line near the top:
   ```java
   private static final String RAW_URL =
           "https://raw.githubusercontent.com/YOUR-GITHUB-USERNAME/YOUR-REPO-NAME/main/tiers.json";
   ```
3. Replace it with the Raw URL you copied in Part 1, step 5.
4. Commit the change.

---

## Part 3 — Get the mod built (no install needed)

1. Create a **second** GitHub repo, e.g. `mccpvptiertagger`. Public is fine.
2. Upload every file from this project into that repo (drag-and-drop works on
   github.com — use **Add file → Upload files**, and keep the folder structure).
3. Go to the **Actions** tab of that repo. A workflow called "Build Mod" will
   run automatically. Wait for the green checkmark (a few minutes).
4. Click into the finished run → scroll to **Artifacts** → download
   `mccpvptiertagger-jar`. Unzip it — that's your mod file (`.jar`).

If the Actions run shows a red ❌ instead of a green checkmark, click into it,
copy the error text, and send it to me — this is normal for a first build and
I'll fix the version numbers for you.

---

## Part 4 — Install it in Minecraft

1. Install **Fabric Loader** for Minecraft 1.21.11 (fabricmc.net/use/installer).
2. Download **Fabric API** for 1.21.11 from Modrinth, put the `.jar` in your `.minecraft/mods` folder.
3. Put the `mccpvptiertagger` `.jar` you built in Part 3 into that same `mods` folder.
4. Launch with the Fabric profile. Tiered players will now show `[TIER] Name` above their head.

---

## Updating tiers later

Just edit `tiers.json` in your first repo (Part 1) and commit. The mod
re-checks that file automatically every 5 minutes — no rebuilding, no
reinstalling, nothing else to do.
