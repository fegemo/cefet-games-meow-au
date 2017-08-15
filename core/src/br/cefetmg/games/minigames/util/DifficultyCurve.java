package br.cefetmg.games.minigames.util;

/**
 * Curvas de dificuldade.
 *
 * Retornar um valor de y dado um valor de x, de acordo com o tipo da curva.
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum DifficultyCurve {

    FLAT {
        @Override
        public float getCurveValue(float value) {
            return 0.5f;
        }
    },
    LINEAR {
        @Override
        public float getCurveValue(float value) {
            return value;
        }
    },
    LINEAR_NEGATIVE {
        @Override
        public float getCurveValue(float value) {
            return -value + 1;
        }
    },
    S {
        @Override
        public float getCurveValue(float value) {
            // desenho da curva:
            // https://www.google.com.br/#safe=off&hl=pt-BR&q=y%3D1%2F(1%2Be%5E(-6*(x-0.5)))
            return (float) (1f / (1f + Math.pow(Math.E, -6 * (value - 0.5f))));
        }
    },
    S_NEGATIVE {
        @Override
        public float getCurveValue(float value) {
            // desenho da curva:
            // https://www.google.com.br/#safe=off&hl=pt-BR&q=y%3D1%2F(1%2Be%5E(6*(x-0.5)))
            return (float) (1f / (1f + Math.pow(Math.E, 6 * (value - 0.5f))));
        }
    };

    /**
     * Retorna um valor entre 0 e 1 de acordo com a curva.
     *
     * @param value o valor de x para qual deseja-se saber o y da curva. O valor
     * de x deve estar entre 0 e 1.
     * @return o valor de y na curva desejada para o valor de x provido.
     */
    public abstract float getCurveValue(float value);

    /**
     * Retorna um valor entre min e max de acordo com a curva.
     *
     * @param value o valor de x para qual deseja-se saber o y da curva. O valor
     * de x deve estar entre 0 e 1.
     * @param min o valor mínimo desejado para y.
     * @param max o valor máximo desejado para y.
     * @return o valor de y na curva desejada para o valor de x provido. Esse
     * valor estará entre min e max.
     */
    public float getCurveValueBetween(float value, float min, float max) {
        return getCurveValue(value) * (max - min) + min;
    }

}
