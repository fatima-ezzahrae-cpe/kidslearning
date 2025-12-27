package com.example.kidslearning

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidslearning.databinding.ActivityAlphabetBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AlphabetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlphabetBinding
    private lateinit var alphabetType: AlphabetType
    private var currentLetterIndex = 0
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private const val EXTRA_ALPHABET_TYPE = "alphabet_type"

        fun newIntent(context: Context, type: AlphabetType): Intent {
            return Intent(context, AlphabetActivity::class.java).apply {
                putExtra(EXTRA_ALPHABET_TYPE, type.name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlphabetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alphabetType = AlphabetType.valueOf(
            intent.getStringExtra(EXTRA_ALPHABET_TYPE) ?: AlphabetType.FRENCH.name
        )

        setupUI()
        loadLetter(currentLetterIndex)
    }

    private fun setupUI() {
        val letters =
            if (alphabetType == AlphabetType.FRENCH) getFrenchAlphabet()
            else getArabicAlphabet()

        binding.apply {
            tvTitle.text =
                if (alphabetType == AlphabetType.FRENCH) "Alphabet Français"
                else "الأبجدية العربية"

            tvProgress.text = "Lettre ${currentLetterIndex + 1}/${letters.size}"

            btnBack.setOnClickListener { finish() }

            btnGrid.setOnClickListener {
                showAllLettersDialog(letters)
            }

            btnListen.setOnClickListener {
                playLetterSound(letters[currentLetterIndex])
            }

            btnPractice.setOnClickListener {
                navigateToPractice(letters[currentLetterIndex])
            }

            btnPrevious.setOnClickListener {
                if (currentLetterIndex > 0) {
                    currentLetterIndex--
                    loadLetter(currentLetterIndex)
                }
            }

            btnNext.setOnClickListener {
                if (currentLetterIndex < letters.size - 1) {
                    currentLetterIndex++
                    loadLetter(currentLetterIndex)
                }
            }
        }
    }

    private fun loadLetter(index: Int) {
        val letters =
            if (alphabetType == AlphabetType.FRENCH) getFrenchAlphabet()
            else getArabicAlphabet()

        val letter = letters[index]

        binding.apply {
            tvLetterCard.text = letter.character
            tvProgress.text = "Lettre ${index + 1}/${letters.size}"

            // ✅ هادو كانوا ناقصين
            tvAnimalName.text = letter.example
            tvExampleText.text = "${letter.character} comme ${letter.example}"

            tvDidYouKnow.text = letter.fact

            // ⚠️ مهم: ivLetterImage خاصو يكون ImageView
            ivLetterImage.setImageResource(letter.imageResId)

            btnPrevious.isEnabled = index > 0
            btnNext.isEnabled = index < letters.size - 1
        }

        playLetterSound(letter)
    }

    private fun showAllLettersDialog(letters: List<Letter>) {
        val dialogView =
            LayoutInflater.from(this).inflate(R.layout.dialog_all_letters, null)

        val recyclerView =
            dialogView.findViewById<RecyclerView>(R.id.rvAllLetters)

        val btnClose =
            dialogView.findViewById<ImageButton>(R.id.btnCloseDialog)

        recyclerView.layoutManager =
            GridLayoutManager(this, 4)

        recyclerView.adapter =
            LetterGridAdapter(letters) { letter ->
                currentLetterIndex = letters.indexOf(letter)
                loadLetter(currentLetterIndex)
            }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .show()

        btnClose.setOnClickListener { dialog.dismiss() }
    }

    private fun playLetterSound(letter: Letter) {
        mediaPlayer?.release()
        mediaPlayer = null

        try {
            val soundName =
                "sound_${letter.character.lowercase()}"

            val soundResId =
                resources.getIdentifier(soundName, "raw", packageName)

            if (soundResId != 0) {
                mediaPlayer = MediaPlayer.create(this, soundResId)
                mediaPlayer?.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigateToPractice(letter: Letter) {
        val intent =
            PracticeActivity.newIntent(this, letter, alphabetType)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // ======== الأبجدية ========
    private fun getFrenchAlphabet(): List<Letter> {
        return listOf(
            Letter("A", "Abeille", "La lettre 'A' est très importante dans beaucoup de mots français", R.drawable.bee),
            Letter("B", "Ballon", "B comme Ballon, l'un des jouets préférés des enfants", R.drawable.football),
            Letter("C", "Chat", "C comme Chat, un animal domestique très mignon", R.drawable.chat),
            Letter("D", "Dauphin", "D comme Dauphin, un animal intelligent de la mer", R.drawable.dauphin),
            Letter("E", "Éléphant", "E comme Éléphant, le plus grand animal terrestre", R.drawable.lelephant),
            Letter("F", "Fleur", "F comme Fleur, belle et parfumée", R.drawable.fleur),
            Letter("G", "Girafe", "G comme Girafe, l'animal avec le plus long cou", R.drawable.girafe),
            Letter("H", "Hippopotame", "H comme Hippopotame, un gros animal d'Afrique", R.drawable.hippopotame),
            Letter("I", "Igloo", "I comme Iglou, une maison de glace", R.drawable.iglou),
            Letter("J", "Jardin", "J comme Jardin, où poussent les fleurs", R.drawable.jardin),
            Letter("K", "Kangourou", "K comme Kangourou, qui saute très haut", R.drawable.kangourou),
            Letter("L", "Lion", "L comme Lion, le roi de la jungle", R.drawable.lion),
            Letter("M", "Mouton", "M comme Mouton, un animal de la ferme", R.drawable.mouton),
            Letter("N", "Nuage", "N comme Nuage, blanc dans le ciel", R.drawable.nuage),
            Letter("O", "Oiseau", "O comme Oiseau, qui vole dans le ciel", R.drawable.oiseau),
            Letter("P", "Papillon", "P comme Papillon, avec de belles couleurs", R.drawable.papillon),
            Letter("Q", "Queue", "Q comme Queue, que les animaux ont", R.drawable.queue),
            Letter("R", "Robot", "R comme Robot, une machine intelligente", R.drawable.robot),
            Letter("S", "Soleil", "S comme Soleil, qui brille dans le ciel", R.drawable.soleil),
            Letter("T", "Tortue", "T comme Tortue, lente mais sage", R.drawable.tortue),
            Letter("U", "Usine", "U comme Usine, où on fabrique des choses", R.drawable.usine),
            Letter("V", "Voiture", "V comme Voiture, pour voyager", R.drawable.voiture),
            Letter("W", "Wagon", "W comme Wagon, partie du train", R.drawable.wagon),
            Letter("X", "Xylophone", "X comme Xylophone, un instrument de musique", R.drawable.xylophone),
            Letter("Y", "Yoyo", "Y comme Yoyo, un jouet amusant", R.drawable.yoyo),
            Letter("Z", "Zèbre", "Z comme Zèbre, avec des rayures noires et blanches", R.drawable.zebre)
        )
    }

    private fun getArabicAlphabet(): List<Letter> {
        return listOf(
            Letter("أ", "أسد (Lion)", "حرف الألف هو أول حرف في الأبجدية العربية", R.drawable.lion),
            Letter("ب", "بطة (Duck)", "حرف الباء له نقطة واحدة تحته", R.drawable.duck),
            Letter("ت", "تفاحة (Pomme)", "حرف التاء له نقطتان فوقه", R.drawable.pomme),
            Letter("ث", "ثعلب (Renard)", "حرف الثاء له ثلاث نقاط فوقه", R.drawable.renard),
            Letter("ج", "جمل (Chameau)", "حرف الجيم له نقطة في الوسط", R.drawable.chameau),
            Letter("ح", "حوت (Baleine)", "حرف الحاء ليس له نقاط", R.drawable.whale),
            Letter("خ", "خروف (Mouton)", "حرف الخاء له نقطة فوقه", R.drawable.mouton),
            Letter("د", "دب (Ours)", "حرف الدال بسيط وجميل", R.drawable.ours),
            Letter("ذ", "ذئب (Loup)", "حرف الذال له نقطة فوقه", R.drawable.loup),
            Letter("ر", "رمان (Grenade)", "حرف الراء منحني", R.drawable.grenade),
            Letter("ز", "زرافة (Girafe)", "حرف الزاي له نقطة فوقه", R.drawable.girafe),
            Letter("س", "سمكة (Poisson)", "حرف السين له ثلاث أسنان", R.drawable.poisson),
            Letter("ش", "شمس (Soleil)", "حرف الشين له ثلاث نقاط فوقه", R.drawable.soleil),
            Letter("ص", "صقر (Faucon)", "حرف الصاد واسع", R.drawable.aigle),
            Letter("ض", "ضفدع (Grenouille)", "حرف الضاد له نقطة فوقه", R.drawable.grenouille),
            Letter("ط", "طائر (Oiseau)", "حرف الطاء طويل", R.drawable.oiseau),
            Letter("ظ", "ظبي (Gazelle)", "حرف الظاء له نقطة فوقه", R.drawable.antilope),
            Letter("ع", "عصفور (Moineau)", "حرف العين مميز", R.drawable.moineau),
            Letter("غ", "غزال (Cerf)", "حرف الغين له نقطة فوقه", R.drawable.cerf),
            Letter("ف", "فيل (Éléphant)", "حرف الفاء له نقطة فوقه", R.drawable.lelephant),
            Letter("ق", "قطة (Chat)", "حرف القاف له نقطتان فوقه", R.drawable.chat),
            Letter("ك", "كلب (Chien)", "حرف الكاف جميل", R.drawable.chien),
            Letter("ل", "ليمون (Citron)", "حرف اللام طويل", R.drawable.citron),
            Letter("م", "ماعز (Chèvre)", "حرف الميم دائري", R.drawable.chevre),
            Letter("ن", "نحلة (Abeille)", "حرف النون له نقطة فوقه", R.drawable.bee),
            Letter("ه", "هدهد (Huppe)", "حرف الهاء بسيط", R.drawable.huppe),
            Letter("و", "وردة (Fleur)", "حرف الواو منحني", R.drawable.fleur),
            Letter("ي", "يد (Main)", "حرف الياء له نقطتان تحته", R.drawable.bonjour)
        )
    }
}

enum class AlphabetType {
    FRENCH, ARABIC
}

data class Letter(
    val character: String,
    val example: String,
    val fact: String,
    val imageResId: Int
)
